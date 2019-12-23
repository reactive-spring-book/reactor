package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class CheckpointTest {

	@Test
	public void checkpoint() throws Exception {

		var stackTrace = new AtomicReference<String>();
		var checkpoint = Flux.just("A", "B", "C", "D").map(String::toLowerCase)
				.flatMapSequential(letter -> {
					if (letter.equals("c")) { // induce the error
						return Mono.error(new IllegalArgumentException("Ooops!"));
					}
					return Flux.just(letter);
				}).checkpoint().delayElements(Duration.ofMillis(1));
		StepVerifier //
				.create(checkpoint) //
				.expectNext("a", "b") //
				.expectErrorMatches(ex -> {
					stackTrace.set(stackTraceToString(ex));
					return ex instanceof IllegalArgumentException;
				}).verify();

		Assert.assertTrue(stackTrace.get()
				.contains("Error has been observed at the following site(s):"));
	}

	private static String stackTraceToString(Throwable throwable) {
		try (var sw = new StringWriter(); var pw = new PrintWriter(sw)) {
			throwable.printStackTrace(pw);
			return sw.toString();
		}
		catch (Exception ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

}
