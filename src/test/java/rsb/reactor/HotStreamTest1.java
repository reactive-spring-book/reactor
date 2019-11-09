package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Log4j2
public class HotStreamTest1 {

	private final int factor = 100;

	@Test
	public void hot() throws Exception {
		log.info("start");
		var cdl = new CountDownLatch(2);
		Flux<Integer> live = this.live().share();
		var one = new ArrayList<Integer>();
		var two = new ArrayList<Integer>();
		live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(one));
		Thread.sleep(this.factor * 2);
		live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(two));
		cdl.await(5, TimeUnit.SECONDS);
		Assert.assertTrue(one.size() > two.size());
		log.info("stop");
	}

	private Consumer<SignalType> signalTypeConsumer(CountDownLatch cdl) {
		return signal -> {
			if (signal.equals(SignalType.ON_COMPLETE)) {
				try {
					cdl.countDown();
					log.info("await()...");
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	private Consumer<Integer> collect(List<Integer> ints) {
		return ints::add;
	}

	private Flux<Integer> live() {
		return Flux.range(0, 10).delayElements(Duration.ofMillis(this.factor));
	}

}
