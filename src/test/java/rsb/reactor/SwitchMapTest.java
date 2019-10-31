package rsb.reactor;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class SwitchMapTest {

	@Test
	public void switchMapTest() throws Exception {

		var letters = Flux.just("a", "b", "c");
		var numbers = Flux.generate(sink -> sink.next(""));

		// Flux<Object> switchMap = data.switchMap(new Function<String, Publisher<?>>() {
		// @Override
		// public Publisher<?> apply(String s) {
		// return null;
		// }
		// });

	}

}
