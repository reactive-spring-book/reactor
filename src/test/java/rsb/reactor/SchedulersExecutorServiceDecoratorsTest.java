package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class SchedulersExecutorServiceDecoratorsTest {

	private final AtomicInteger methodInvocationCounts = new AtomicInteger();

	private String rsb = "rsb";

	@Before
	public void before() {
		Schedulers.resetFactory();
		Schedulers.addExecutorServiceDecorator(this.rsb, (scheduler,
				scheduledExecutorService) -> this.decorate(scheduledExecutorService));
	}

	@Test
	public void changeDefaultDecorator() {
		Flux<Integer> integerFlux = Flux.just(1).delayElements(Duration.ofMillis(1));
		StepVerifier.create(integerFlux).thenAwait(Duration.ofMillis(10))
				.expectNextCount(1).verifyComplete();
		Assert.assertEquals(1, this.methodInvocationCounts.get());
	}

	@After
	public void after() {
		Schedulers.resetFactory();
		Schedulers.removeExecutorServiceDecorator(this.rsb);
	}

	private ScheduledExecutorService decorate(ScheduledExecutorService executorService) {
		try {
			var pfb = new ProxyFactoryBean();
			pfb.setProxyInterfaces(new Class[] { ScheduledExecutorService.class });
			pfb.addAdvice((MethodInterceptor) methodInvocation -> {
				var methodName = methodInvocation.getMethod().getName().toLowerCase();
				this.methodInvocationCounts.incrementAndGet();
				log.info("methodName: (" + methodName + ") incrementing...");
				return methodInvocation.proceed();
			});
			pfb.setSingleton(true);
			pfb.setTarget(executorService);
			return (ScheduledExecutorService) pfb.getObject();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}

}
