package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class OrchestrationTest {

	@Test
	public void merge() {
		Flux<Integer> fastest = Flux.just(5, 6);
		Flux<Integer> secondFastest = Flux.just(1, 2).delayElements(Duration.ofMillis(2));
		Flux<Integer> thirdFastest = Flux.just(3, 4).delayElements(Duration.ofMillis(20));
		Flux<Flux<Integer>> streamOfStreams = Flux.just(secondFastest, thirdFastest,
				fastest);
		Flux<Integer> merge = Flux.merge(streamOfStreams);
		StepVerifier.create(merge).expectNext(5, 6, 1, 2, 3, 4).verifyComplete();
	}

	@Test
	public void retry() {
		AtomicBoolean errored = new AtomicBoolean();
		// can we make retry() work? how do we demo it?
		Flux<String> producer = Flux.create(sink -> {
			if (!errored.get()) {
				errored.set(true);
				sink.error(new RuntimeException("Nope!"));
				log.info("returning a " + RuntimeException.class.getName() + "!");
			}
			else {
				log.info("we've already errored so here's the value");
				sink.next("hello");
			}
			sink.complete();
		});

		Flux<String> retryOnError = producer.retry();
		StepVerifier.create(retryOnError).expectNext("hello").verifyComplete();
	}

	Flux<String> callFlakyService() {
		if (Math.random() > .5) {
			return Flux.just("hello");
		}
		else {
			return Flux.error(new IllegalArgumentException("nope!"));
		}
	}

	@Test
	public void zip() {
		Flux<Integer> first = Flux.just(1, 2, 3);
		Flux<String> second = Flux.just("a", "b", "c");
		Flux<String> zip = Flux.zip(first, second)
				.map(tuple -> tuple.getT1() + ":" + tuple.getT2());
		StepVerifier.create(zip).expectNext("1:a", "2:b", "3:c").verifyComplete();
	}

	@Test
	public void first() {
		Flux<Integer> slow = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(10));
		Flux<Integer> fast = Flux.just(4, 5, 6).delayElements(Duration.ofMillis(2));
		Flux<Integer> first = Flux.first(slow, fast);
		StepVerifier.create(first).expectNext(4, 5, 6).verifyComplete();
	}

}
