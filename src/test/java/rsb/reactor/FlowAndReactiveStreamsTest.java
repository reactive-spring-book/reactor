package rsb.reactor;

import org.junit.Test;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.Flow;

public class FlowAndReactiveStreamsTest {

	@Test
	public void convert() {
		// <1>
		Flux<Integer> original = Flux.range(0, 10);

		Flow.Publisher<Integer> rangeOfIntegersAsJdk9Flow = FlowAdapters
				.toFlowPublisher(original);
		Publisher<Integer> rangeOfIntegersAsReactiveStream = FlowAdapters
				.toPublisher(rangeOfIntegersAsJdk9Flow);

		StepVerifier.create(original).expectNextCount(10).verifyComplete();

		StepVerifier.create(rangeOfIntegersAsReactiveStream).expectNextCount(10)
				.verifyComplete();

		// <2>
		Flux<Integer> rangeOfIntegersAsReactorFluxAgain = JdkFlowAdapter
				.flowPublisherToFlux(rangeOfIntegersAsJdk9Flow);
		StepVerifier.create(rangeOfIntegersAsReactorFluxAgain).expectNextCount(10)
				.verifyComplete();

	}

}
