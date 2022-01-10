package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ControlFlowZipTest {

	@Test
	public void zip() {
		var first = Flux.just(1, 2, 3);
		var second = Flux.just("a", "b", "c");
		var zip = Flux.zip(first, second).map(tuple -> this.from(tuple.getT1(), tuple.getT2()));
		StepVerifier.create(zip).expectNext("1:a", "2:b", "3:c").verifyComplete();
	}

	private String from(Integer i, String s) {
		return i + ":" + s;
	}

}
