package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class OnErrorReturnTest {

	private final Flux<Integer> resultsInError = Flux.just(1, 2, 3).flatMap(counter -> {
		if (counter == 2) {
			return Flux.error(new IllegalArgumentException("Oops!"));
		}
		else {
			return Flux.just(counter);
		}
	});

	@Test
	public void onErrorReturn() {
		Flux<Integer> integerFlux = resultsInError.onErrorReturn(0);
		StepVerifier.create(integerFlux).expectNext(1, 0).verifyComplete();
	}

}
