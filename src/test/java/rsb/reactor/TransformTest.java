package rsb.reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

public class TransformTest {

	@Test
	public void transform() {
		var finished = new AtomicBoolean();
		var letters = Flux//
				.just("A", "B", "C").transform(stringFlux -> stringFlux.doFinally(signal -> finished.set(true)));// <1>
		StepVerifier.create(letters).expectNextCount(3).verifyComplete();
		Assertions.assertTrue(finished.get(), "the finished Boolean must be true.");
	}

}
