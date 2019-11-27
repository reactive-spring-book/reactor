package rsb.reactor;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;

@Log4j2
public class BlockhoundTest {

	@Before
	public void install() {
		BlockHound.install();
	}

	@Test
	public void notOk() {
		StepVerifier.create(this.buildBlockingMono().subscribeOn(Schedulers.parallel()))
				.expectErrorMatches(e -> {
					e.printStackTrace();
					return e instanceof Error
							&& e.getMessage().contains("Blocking call!");
				}).verify();
	}

	@Test
	public void ok() {
		StepVerifier.create(this.buildBlockingMono().subscribeOn(Schedulers.elastic())) //
				.expectNext(1L)//
				.verifyComplete();
	}

	private Mono<Long> buildBlockingMono() {
		return Mono.just(1L).doOnNext(it -> block());
	}

	@SneakyThrows
	private void block() {
		Thread.sleep(1000);
	}

}