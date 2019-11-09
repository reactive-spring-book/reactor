package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Log4j2
public class ColdStreamTest {

	@Test
	public void cold() {
		Flux<Integer> cold = Flux.just(1, 2, 3);
		StepVerifier.create(cold).expectNext(1, 2, 3).verifyComplete();
		StepVerifier.create(cold).expectNext(1, 2, 3).verifyComplete();

		Flux<Integer> delayed = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(3));
		StepVerifier.create(delayed).expectNext(1, 2, 3).verifyComplete();
		StepVerifier.create(delayed).expectNext(1, 2, 3).verifyComplete();
	}

}
