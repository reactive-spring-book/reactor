package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
public class HandleTest {

	@Test
	public void handle() throws Exception {

		Flux<Integer> values = Flux//
				.range(0, 5).handle((value, sink) -> {
					if (value % 2 == 0) {
						sink.next(value);
					}
				});
		StepVerifier.create(values).expectNext(0, 2, 4).verifyComplete();
	}

}
