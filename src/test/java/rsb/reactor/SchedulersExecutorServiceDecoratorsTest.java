package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class SchedulersExecutorServiceDecoratorsTest {

	private final AtomicInteger scheduleCounts = new AtomicInteger();

	private final AtomicReference<ScheduledExecutorService> cached = new AtomicReference<>();

	private String rsb = "rsb";

	@Test
	public void changeDefaultDecorator() {
		Schedulers.resetFactory();
		Schedulers.addExecutorServiceDecorator(this.rsb, (scheduler,
				scheduledExecutorService) -> this.decorate(scheduledExecutorService));
		Scheduler scheduler = Schedulers.immediate();
		Flux<Integer> integerFlux = Flux.just(1).delayElements(Duration.ofMillis(1))
				.subscribeOn(scheduler);
		StepVerifier.create(integerFlux).thenAwait(Duration.ofMillis(10))
				.expectNextCount(1).verifyComplete();
		Assert.assertEquals(1, this.scheduleCounts.get());
		Schedulers.resetFactory();
	}

	private ScheduledExecutorService decorate(ScheduledExecutorService executorService) {
		try {
			var pfb = new ProxyFactoryBean();
			pfb.setProxyInterfaces(new Class[] { ScheduledExecutorService.class });
			pfb.addAdvice((MethodInterceptor) methodInvocation -> {
				var methodName = methodInvocation.getMethod().getName().toLowerCase();
				this.scheduleCounts.incrementAndGet();
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
