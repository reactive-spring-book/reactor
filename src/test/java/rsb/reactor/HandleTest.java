package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

@Log4j2
public class HandleTest {

	@Test
	public void handle() throws Exception {

		Flux<Integer> range = Flux.range(0, 5).handle((value, sink) -> {
			var upToThree = Arrays.asList(0, 1, 2, 3);
			if (upToThree.contains(value)) {
				sink.next(value);
				return;
			}
			if (value == 4) {
				sink.error(new IllegalArgumentException("No 4 for you!"));
				return;
			}
			sink.complete();
		});
		StepVerifier//
				.create(range)//
				.expectNext(0, 1, 2, 3)//
				.expectError(IllegalArgumentException.class)//
				.verify();
	}

}
