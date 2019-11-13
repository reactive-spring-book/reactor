package rsb.reactor;

import org.junit.Test;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

public class EmitterProcessorTest {

	@Test
	public void emitterProcessor() {
		EmitterProcessor<String> processor = EmitterProcessor.create();
		produce(processor.sink());
		consume(processor);
	}

	private void produce(FluxSink<String> sink) {
		sink.next("1");
		sink.next("2");
		sink.next("3");
		sink.complete();
	}

	private void consume(Flux<String> publisher) {
		StepVerifier.create(publisher).expectNext("1").expectNext("2").expectNext("3")
				.verifyComplete();
	}

}
