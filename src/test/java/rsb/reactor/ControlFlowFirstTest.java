package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Slf4j
public class ControlFlowFirstTest {

	@Test
	public void first() {
		var slow = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(10));
		var fast = Flux.just(4, 5, 6, 7).delayElements(Duration.ofMillis(2));
		var first = Flux.firstWithSignal(slow, fast);
		StepVerifier.create(first).expectNext(4, 5, 6, 7).verifyComplete();
	}

}
