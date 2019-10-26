package rsb.reactor;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncApiIntegrationTest {

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	@Test
	public void async() throws Exception {
		Flux<Integer> integers = Flux.create(emitter -> this.launch(emitter, 5));

		StepVerifier
				.create(integers.doFinally(signalType -> this.executorService.shutdown()))
				.expectNextCount(5).verifyComplete();
	}

	// NB: you need to setup whatever connections with an extenal API ONLy after you're inside the callback
	private void launch(FluxSink<Integer> integerFluxSink, int count) {
		this.executorService.submit(new Runnable() {

			@SneakyThrows
			@Override
			public void run() {
				AtomicInteger integer = new AtomicInteger();
				Assert.assertNotNull(integerFluxSink);
				while (integer.get() < count) {
					double random = Math.random();
					integerFluxSink.next(integer.incrementAndGet());
					Thread.sleep((long) (random * 1_000));
				}
				integerFluxSink.complete();
			}
		});
	}

}
