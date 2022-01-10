package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Slf4j
public class ColdStreamTest {

	@Test
	public void cold() {
		var cold = Flux.just(1, 2, 3);
		StepVerifier.create(cold).expectNext(1, 2, 3).verifyComplete();
		StepVerifier.create(cold).expectNext(1, 2, 3).verifyComplete();

		var delayed = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(3));
		StepVerifier.create(delayed).expectNext(1, 2, 3).verifyComplete();
		StepVerifier.create(delayed).expectNext(1, 2, 3).verifyComplete();
	}

}
