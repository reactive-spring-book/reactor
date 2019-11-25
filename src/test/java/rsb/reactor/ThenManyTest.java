package rsb.reactor;

import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

public class ThenManyTest {

	@Test
	public void thenMany() {
		var letters = new AtomicInteger();
		var numbers = new AtomicInteger();
		Flux<String> lettersPublisher = Flux.just("a", "b", "c")
				.doOnNext(value -> letters.incrementAndGet());
		Flux<Integer> numbersPublisher = Flux.just(1, 2, 3)
				.doOnNext(number -> numbers.incrementAndGet());
		Flux<Integer> thisBeforeThat = lettersPublisher.thenMany(numbersPublisher);
		StepVerifier.create(thisBeforeThat).expectNext(1, 2, 3).verifyComplete();
		Assert.assertEquals(letters.get(), 3);
		Assert.assertEquals(numbers.get(), 3);
	}

}
