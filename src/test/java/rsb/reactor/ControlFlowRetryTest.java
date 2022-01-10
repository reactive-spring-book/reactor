package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ControlFlowRetryTest {

	@Test
	public void retry() {

		var errored = new AtomicBoolean();
		var producer = Flux.<String>create(sink -> {
			if (!errored.get()) {
				errored.set(true);
				sink.error(new RuntimeException("Nope!"));
				log.info("returning a " + RuntimeException.class.getName() + "!");
			}
			else {
				log.info("we've already errored so here's the value");
				sink.next("hello");
			}
			sink.complete();
		});
		var retryOnError = producer.retry();
		StepVerifier.create(retryOnError).expectNext("hello").verifyComplete();
	}

}
