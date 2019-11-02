package rsb.reactor;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ConcatMapTest {

	private final List<Integer> concurrentList = new ArrayList<>();

	@Before
	public void reset() {
		this.concurrentList.clear();
	}

	@Test
	public void concatMap() {
		var max = 5;
		Flux<Integer> data = Flux.range(0, max).concatMap(this::add);
		StepVerifier.create(data).expectNext(0, 1, 2, 3, 4).verifyComplete();
		for (int i = 0; i < max; i++)
			Assert.assertEquals(this.concurrentList.get(i), (Integer) i);
	}

	private Publisher<Integer> add(Integer a) {
		return Flux.<Integer>create(sink -> {
			var random = Math.random();
			var sleep = (long) (random * 1_000);
			try {
				log.info("sleeping " + sleep);
				Thread.sleep(sleep);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			this.concurrentList.add(a);
			sink.next(a);
			sink.complete();
		})//
				.subscribeOn(Schedulers.boundedElastic());
	}

}
