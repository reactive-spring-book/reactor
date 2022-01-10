package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Slf4j
public class ConcatMapTest {

	@Test
	public void concatMap() {
		var data = Flux.just(new Pair(1, 300), new Pair(2, 200), new Pair(3, 100))
				.concatMap(id -> this.delayReplyFor(id.id, id.delay));
		StepVerifier//
				.create(data)//
				.expectNext(1, 2, 3)//
				.verifyComplete();
	}

	private Flux<Integer> delayReplyFor(Integer i, long delay) {
		return Flux.just(i).delayElements(Duration.ofMillis(delay));
	}

	private record Pair(int id, long delay) {
	}

}
