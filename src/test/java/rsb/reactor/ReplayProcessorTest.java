package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import reactor.test.StepVerifier;

public class ReplayProcessorTest {

	@Test
	public void replayProcessor() {
		int historySize = 2;
		boolean unbounded = false;
		ReplayProcessor<String> processor = ReplayProcessor.create(historySize, unbounded); // <1>
		produce(processor.sink());
		consume(processor);
	}

	// <2>
	private void produce(FluxSink<String> sink) {
		sink.next("1");
		sink.next("2");
		sink.next("3");
		sink.complete();
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
