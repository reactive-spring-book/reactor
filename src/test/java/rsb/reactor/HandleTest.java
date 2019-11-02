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
				.range(0, 7)//
				.handle((value, sink) -> {
					if (value == 6) { // trigger completion
						sink.complete();
						return;
					}
					if (value % 2 == 0) { // emit only the even numbers below 6
						sink.next(value);
					}
				});
		StepVerifier.create(values).expectNext(0, 2, 4).verifyComplete();
	}

}
