package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
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

	private final AtomicInteger scheduleCounts = new AtomicInteger();

	@Test
	public void changeDefaults() throws Exception {
		Schedulers.addExecutorServiceDecorator("rsb", (scheduler,
				scheduledExecutorService) -> this.proxy(scheduledExecutorService));
		Flux<Integer> integerFlux = Flux.just(1, 2, 3)
				.delayElements(Duration.ofMillis(1));
		StepVerifier.create(integerFlux).expectNextCount(3).verifyComplete();
		Assert.assertEquals(this.scheduleCounts.get(), 3);
	}

	private ScheduledExecutorService proxy(ScheduledExecutorService executorService) {
		try {
			var pfb = new ProxyFactoryBean();
			pfb.addAdvice((MethodInterceptor) methodInvocation -> {
				var methodName = methodInvocation.getMethod().getName().toLowerCase();
				if (methodName.startsWith("schedule")) {
					this.scheduleCounts.incrementAndGet();
				}
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
