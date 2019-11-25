package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
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
