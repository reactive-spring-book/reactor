package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class AsyncApiIntegrationTest {

	private final ExecutorService executorService = Executors.newFixedThreadPool(1);

	@Test
	public void async() {
		Flux<Integer> integers = Flux.create(emitter -> this.launch(emitter, 5));

		StepVerifier
				.create(integers.doFinally(signalType -> this.executorService.shutdown()))
				.expectNextCount(5).verifyComplete();
	}

	// NB: you need to setup whatever connections with an external API ONLy after you're
	// inside the callback
	private void launch(FluxSink<Integer> integerFluxSink, int count) {
		this.executorService.submit(() -> {
			AtomicInteger integer = new AtomicInteger();
			Assert.assertNotNull(integerFluxSink);
			while (integer.get() < count) {
				double random = Math.random();
				integerFluxSink.next(integer.incrementAndGet());
				this.sleep((long) (random * 1_000));
			}
			integerFluxSink.complete();
		});
	}

	private void sleep(long s) {
		try {
			Thread.sleep(s);
		}
		catch (Exception e) {
			log.error(e);
		}
	}

}
