package com.example.demo;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
class ProfileCreatedEventPublisher implements
	ApplicationListener<ProfileCreatedEvent>, // <1>
	Consumer<FluxSink<ProfileCreatedEvent>> { //<2>

	private final Executor executor;
	private final BlockingQueue<ProfileCreatedEvent> queue =
		new LinkedBlockingQueue<>(); // <3>

	ProfileCreatedEventPublisher(Executor executor) {
		this.executor = executor;
	}

	// <4>
	@Override
	public void onApplicationEvent(ProfileCreatedEvent event) {
		this.queue.offer(event);
	}

	@Override
	public void accept(FluxSink<ProfileCreatedEvent> sink) {
		this.executor.execute(() -> {
			while (true)
				try {
					ProfileCreatedEvent event = queue.take(); // <5>
					sink.next(event); // <6>
				}
				catch (InterruptedException e) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
		});
	}
}
