package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class TakeTest {

	@Test
	public void take() {
		var count = 10;
		var take = range().take(count);
		StepVerifier.create(take).expectNextCount(count).verifyComplete();
	}

	@Test
	public void takeUntil() {
		var count = 50;
		var take = range().takeUntil(i -> i == (count - 1));
		StepVerifier.create(take).expectNextCount(count).verifyComplete();
	}

	private Flux<Integer> range() {
		return Flux.range(0, 1000);
	}

}
