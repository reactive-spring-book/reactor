package rsb.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MapTest {

	@Test
	public void maps() {
		var data = Flux.just("a", "b", "c").map(String::toUpperCase);
		StepVerifier.create(data).expectNext("A", "B", "C").verifyComplete();

	}

}
