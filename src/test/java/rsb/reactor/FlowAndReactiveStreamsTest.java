package rsb.reactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.FlowAdapters;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FlowAndReactiveStreamsTest {

	@Test
	public void convert() {
		// <1>
		var original = Flux.range(0, 10);
		var rangeOfIntegersAsJdk9Flow = FlowAdapters.toFlowPublisher(original);
		var rangeOfIntegersAsReactiveStream = FlowAdapters.toPublisher(rangeOfIntegersAsJdk9Flow);

		StepVerifier.create(original).expectNextCount(10).verifyComplete();
		StepVerifier.create(rangeOfIntegersAsReactiveStream).expectNextCount(10).verifyComplete();

		// <2>
		var rangeOfIntegersAsReactorFluxAgain = JdkFlowAdapter.flowPublisherToFlux(rangeOfIntegersAsJdk9Flow);
		StepVerifier.create(rangeOfIntegersAsReactorFluxAgain).expectNextCount(10).verifyComplete();

	}

}
