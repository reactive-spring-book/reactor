package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class SimpleFluxFactoriesTest {

	@Test
	public void simple() {

		// <1>
		var rangeOfIntegers = Flux.range(0, 10);
		StepVerifier.create(rangeOfIntegers).expectNextCount(10).verifyComplete();

		// <2>
		var letters = Flux.just("A", "B", "C");
		StepVerifier.create(letters).expectNext("A", "B", "C").verifyComplete();

		// <3>
		var now = System.currentTimeMillis();
		var greetingMono = Mono.just(new Date(now));
		StepVerifier.create(greetingMono).expectNext(new Date(now)).verifyComplete();

		// <4>
		var empty = Mono.empty();
		StepVerifier.create(empty).verifyComplete();

		// <5>
		var fromArray = Flux.fromArray(new Integer[] { 1, 2, 3 });
		StepVerifier.create(fromArray).expectNext(1, 2, 3).verifyComplete();

		// <6>
		var fromIterable = Flux.fromIterable(Arrays.asList(1, 2, 3));
		StepVerifier.create(fromIterable).expectNext(1, 2, 3).verifyComplete();

		// <7>
		var integer = new AtomicInteger();
		var integerFlux = Flux.fromStream(Stream.generate(integer::incrementAndGet));
		StepVerifier.create(integerFlux.take(3)).expectNext(1).expectNext(2).expectNext(3).verifyComplete();
	}

}