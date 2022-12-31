package rsb.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class HotStreamTest2 {

	@Test
	public void hot() throws Exception {
		var factor = 10;
		log.info("start");
		var cdl = new CountDownLatch(2);
		var live = Flux.range(0, 10).delayElements(Duration.ofMillis(factor)).share();
		var one = new ArrayList<Integer>();
		var two = new ArrayList<Integer>();
		live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(one));
		Thread.sleep(factor * 2);
		live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(two));
		cdl.await(5, TimeUnit.SECONDS);
		Assertions.assertTrue(one.size() > two.size());
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

}
