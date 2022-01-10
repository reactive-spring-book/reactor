package rsb.reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

public class ThenManyTest {

	@Test
	public void thenMany() {
		var letters = new AtomicInteger();
		var numbers = new AtomicInteger();
		var lettersPublisher = Flux.just("a", "b", "c").doOnNext(value -> letters.incrementAndGet());
		var numbersPublisher = Flux.just(1, 2, 3).doOnNext(number -> numbers.incrementAndGet());
		var thisBeforeThat = lettersPublisher.thenMany(numbersPublisher);
		StepVerifier.create(thisBeforeThat).expectNext(1, 2, 3).verifyComplete();
		Assertions.assertEquals(letters.get(), 3);
		Assertions.assertEquals(numbers.get(), 3);
	}

}
