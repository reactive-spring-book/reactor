package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
public class FilterTest {

	@Test
	public void filter() {
		Flux<Integer> range = Flux.range(0, 1000).take(5);
		Flux<Integer> filter = range.filter(i -> i % 2 == 0);
		StepVerifier.create(filter).expectNext(0, 2, 4).verifyComplete();
	}

}
