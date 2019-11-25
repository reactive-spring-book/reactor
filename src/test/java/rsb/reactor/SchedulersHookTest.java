package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class SchedulersHookTest {

	@Test
	public void onScheduleHook() {
		var counter = new AtomicInteger();
		Schedulers.onScheduleHook("my hook", runnable -> () -> {
			var threadName = Thread.currentThread().getName();
			counter.incrementAndGet();
			log.info("before execution: " + threadName);
			runnable.run();
			log.info("after execution: " + threadName);
		});
		Flux<Integer> integerFlux = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(1))
				.subscribeOn(Schedulers.immediate());
		StepVerifier.create(integerFlux).expectNext(1, 2, 3).verifyComplete();
		Assert.assertEquals("count should be 3", 3, counter.get());
	}

}
