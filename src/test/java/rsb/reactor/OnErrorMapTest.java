package rsb.reactor;

import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

public class OnErrorMapTest {

	@Test
	public void onErrorMap() throws Exception {
		class GenericException extends RuntimeException {

		}
		var counter = new AtomicInteger();
		Flux<Integer> resultsInError = Flux.error(new IllegalArgumentException("oops!"));
		Flux<Integer> errorHandlingStream = resultsInError
				.onErrorMap(IllegalArgumentException.class, ex -> new GenericException())
				.doOnError(GenericException.class, ge -> counter.incrementAndGet());
		StepVerifier.create(errorHandlingStream).expectError().verify();
		Assert.assertEquals(counter.get(), 1);
	}

}
