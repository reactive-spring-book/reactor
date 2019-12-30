package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class HooksOnOperatorDebugTest {

	@Test
	public void onOperatorDebug() throws Exception {
		Hooks.onOperatorDebug();
		var stackTrace = new AtomicReference<String>();
		var errorFlux = Flux//
				.just("a", "b", "c", "d")//
				.flatMapSequential(letter -> {
					if (letter.equals("c")) { // induce the error
						return Mono.error(new IllegalArgumentException("Ooops!"));
					}
					return Flux.just(letter);
				})//
				.checkpoint()//
				.delayElements(Duration.ofMillis(1));

		StepVerifier //
				.create(errorFlux) //
				.expectNext("a", "b") //
				.expectErrorMatches(ex -> {//
					stackTrace.set(stackTraceToString(ex));
					return ex instanceof IllegalArgumentException;
				})//
				.verify();
		Assert.assertTrue(stackTrace.get()
				.contains("Mono.error ⇢ at " + HooksOnOperatorDebugTest.class.getName()));
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
