package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
public class HandleTest {

	@Test
	public void handle() throws Exception {
		/*
		 * // todo fix this and make sure it shows how to emit an error too! Flux<Integer>
		 * values = Flux// .range(0, 7)// .handle((value, sink) -> { if (value % 2 == 0) {
		 * // emit only the even numbers below 6 sink.next(value); return; }
		 *//*
			 * if (value == 6) { sink.error(new IllegalArgumentException()); }
			 *//*
				 * sink.complete(); }); StepVerifier.create(values).expectNext(0, 2,
				 * 4).verifyComplete();
				 */
	}

}
