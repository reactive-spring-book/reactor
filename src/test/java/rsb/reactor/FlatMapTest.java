package rsb.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FlatMapTest {

	@Test
	public void flatMap() {
		Flux<String> data = Flux.just("a", "b", "c");
		Flux<String> both = data.flatMap(this::both);
		StepVerifier.create(both).expectNext("a", "A", "b", "B", "c", "C")
				.verifyComplete();
	}

	private Flux<String> both(String c) {
		return Flux.just(c.toLowerCase(), c.toUpperCase());
	}

}
