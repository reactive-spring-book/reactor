package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

// todo boot3
public class ReplayProcessorTest {

	@Test
	public void replayProcessor() {
		var historySize = 2;
		var processor = Sinks.many().replay().<String>limit(historySize);
		produce(processor);
		consume(processor.asFlux());
	}

	// <2>
	private void produce(Sinks.Many<String> sink) {
		for (var i = 0; i < 3; i++)
			sink.tryEmitNext((i + 1) + "");
		sink.tryEmitComplete();
	}

	// <3>
	private void consume(Flux<String> publisher) {
		for (int i = 0; i < 5; i++)
			StepVerifier//
					.create(publisher)//
					.expectNext("2")//
					.expectNext("3")//
					.verifyComplete();
	}

}
