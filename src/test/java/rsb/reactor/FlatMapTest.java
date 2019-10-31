package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class FlatMapTest {

	private final List<Integer> concurrentList = new ArrayList<>();

	@Before
	public void reset() {
		this.concurrentList.clear();
	}

	@Test
	public void flatMap() {
		Assert.assertTrue(false);
		var count = 0;
		var outOutOfOrder = false;
		while (count++ < 3) {
			log.info("loop " + count);
			if (outOutOfOrder = this.attemptToObserveOutOfOrderResults()) {
				break;
			}
		}
		Assert.assertTrue("the elements should be out of order, eventually.",
				outOutOfOrder);
	}

	private boolean attemptToObserveOutOfOrderResults() {
		int max = 5;
		Flux<Integer> data = Flux.range(0, max).flatMap(this::add);
		StepVerifier.create(data).expectNextCount(max).verifyComplete();
		for (int i = 0; i < max; i++) {
			if (concurrentList.get(i) != i) {
				return true;
			}
		}
		return false;
	}

	private Publisher<Integer> add(Integer a) {
		return Flux.<Integer>create(sink -> {
			var random = Math.random();
			var sleep = (long) (random * 1_000);
			try {
				Thread.sleep(sleep);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			this.concurrentList.add(a);
			sink.next(a);
			sink.complete();
		}).subscribeOn(Schedulers.boundedElastic());
	}

}
