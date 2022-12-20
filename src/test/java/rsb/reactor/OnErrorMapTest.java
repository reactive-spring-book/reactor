package rsb.reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

public class OnErrorMapTest {

	static class GenericException extends RuntimeException {

	}

	@Test
	public void onErrorMap() throws Exception {

		var counter = new AtomicInteger();
		Flux<Integer> resultsInError = Flux.error(new IllegalArgumentException("oops!"));
		Flux<Integer> errorHandlingStream = resultsInError
				.onErrorMap(IllegalArgumentException.class, ex -> new GenericException())
				.doOnError(GenericException.class, ge -> counter.incrementAndGet());
		StepVerifier.create(errorHandlingStream).expectError().verify();
		Assertions.assertEquals(counter.get(), 1);
	}

}
