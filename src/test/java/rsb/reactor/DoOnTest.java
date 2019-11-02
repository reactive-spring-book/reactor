package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Log4j2
public class DoOnTest {

	@Test
	public void doOn() {

		Flux<String> on = Flux.just("1", "2", "3")
				.doOnNext(value -> log.info("new value: " + value))
				.doOnSubscribe(subscription -> {
					log.info("subscribed!");
				}).doOnError(IllegalArgumentException.class, ex -> {
					log.error("oops!", ex);
				}).doFinally(signal -> {
					log.info("completing with signal " + signal.toString());
				});

	}

}
