package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Log4j2
public class SwitchMapTest {

	@Test
	public void switchMap() {
		Flux<Integer> source = Flux.just(1, 2, 3)
				.switchMap(i -> Flux.just(i).delayElements(Duration.ofMillis(100)));
		StepVerifier.create(source).expectNext(3).verifyComplete();
	}

	@Test
	public void switchMapWithLookaheads() {
		Flux<String> source = Flux.just("re", "rea", "reac", "react", "reactive")
				.delayElements(Duration.ofMillis(100)).switchMap(this::lookup);
		StepVerifier.create(source).expectNext("reactive -> reactive").verifyComplete();
	}

	private Flux<String> lookup(String word) {
		return Flux.just(word + " -> reactive").delayElements(Duration.ofMillis(500));
	}

}
