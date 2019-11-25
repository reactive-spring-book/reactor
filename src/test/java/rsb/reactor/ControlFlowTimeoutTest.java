package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Log4j2
public class ControlFlowTimeoutTest {

	@Test
	public void timeout() throws Exception {

		Flux<Integer> ids = Flux.just(1, 2, 3).delayElements(Duration.ofSeconds(1))
				.timeout(Duration.ofMillis(500)).onErrorResume(this::given);

		StepVerifier.create(ids).expectNext(0).verifyComplete();
	}

	private Flux<Integer> given(Throwable t) {
		Assert.assertTrue(
				"this exception should be a " + TimeoutException.class.getName(),
				t instanceof TimeoutException);
		return Flux.just(0);
	}

}
