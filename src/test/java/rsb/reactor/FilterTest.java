package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class FilterTest {

	@Test
	public void filter() {
		var range = Flux.range(0, 1000).take(5);
		var filter = range.filter(i -> i % 2 == 0);
		StepVerifier.create(filter).expectNext(0, 2, 4).verifyComplete();
	}

}
