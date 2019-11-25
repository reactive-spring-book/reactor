package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class ControlFlowFirstTest {

	@Test
	public void first() {
		Flux<Integer> slow = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(10));
		Flux<Integer> fast = Flux.just(4, 5, 6).delayElements(Duration.ofMillis(2));
		Flux<Integer> first = Flux.first(slow, fast);
		StepVerifier.create(first).expectNext(4, 5, 6).verifyComplete();
	}

}
