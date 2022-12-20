package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SchedulersExecutorServiceDecoratorsTest {

	private final AtomicInteger methodInvocationCounts = new AtomicInteger();

	private String rsb = "rsb";

	private ScheduledExecutorService decorate(ScheduledExecutorService executorService) {
		try {
			var pfb = new ProxyFactory();
			pfb.setInterfaces(ScheduledExecutorService.class);
			pfb.addAdvice((MethodInterceptor) methodInvocation -> {
				var methodName = methodInvocation.getMethod().getName().toLowerCase();
				this.methodInvocationCounts.incrementAndGet();
				log.info("methodName: (" + methodName + ") incrementing...");
				return methodInvocation.proceed();
			});
			pfb.setTarget(executorService);
			return (ScheduledExecutorService) pfb.getProxy();
		}
		catch (Exception e) {
			log.error("something went wrong!", e);
		}
		return null;
	}

	@BeforeEach
	public void before() {
		// <1>
		Schedulers.resetFactory();
		// <2>
		Schedulers.addExecutorServiceDecorator(this.rsb,
				(scheduler, scheduledExecutorService) -> this.decorate(scheduledExecutorService));
	}

	@Test
	public void changeDefaultDecorator() {
		var integerFlux = Flux.just(1).delayElements(Duration.ofMillis(1));
		StepVerifier.create(integerFlux).thenAwait(Duration.ofMillis(10)).expectNextCount(1).verifyComplete();
		Assertions.assertEquals(1, this.methodInvocationCounts.get());
	}

	@AfterEach
	public void after() {
		Schedulers.resetFactory();
		Schedulers.removeExecutorServiceDecorator(this.rsb);
	}

}
