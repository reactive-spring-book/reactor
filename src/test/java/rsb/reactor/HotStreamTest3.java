package rsb.reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HotStreamTest3 {

	private List<Integer> one = new ArrayList<Integer>();

	private List<Integer> two = new ArrayList<Integer>();

	private List<Integer> three = new ArrayList<Integer>();

	private Consumer<Integer> subscribe(List<Integer> list) {
		return list::add;
	}

	@Test
	public void publish() throws Exception {

		Flux<Integer> pileOn = Flux.just(1, 2, 3).publish().autoConnect(3).subscribeOn(Schedulers.immediate()); // <1>

		pileOn.subscribe(subscribe(one));
		Assertions.assertEquals(this.one.size(), 0);

		pileOn.subscribe(subscribe(two));
		Assertions.assertEquals(this.two.size(), 0);

		pileOn.subscribe(subscribe(three));
		Assertions.assertEquals(this.three.size(), 3);
		Assertions.assertEquals(this.two.size(), 3);
		Assertions.assertEquals(this.three.size(), 3);
	}

}
