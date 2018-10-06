package com.example.io;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Log4j2
class Asynchronous implements Reader, CompletionHandler<Integer, ByteBuffer> {

	private final ExecutorService executorService = Executors.newFixedThreadPool(10);
	private int bytesRead;
	private long position;
	private AsynchronousFileChannel fileChannel;
	private Consumer<BytesPayload> consumer;

	public void read(File file, Consumer<BytesPayload> c) throws IOException {
		this.consumer = c;
		Path path = file.toPath(); // <1>
		this.fileChannel = AsynchronousFileChannel.open(path,
			Collections.singleton(StandardOpenOption.READ), this.executorService); // <2>
		ByteBuffer buffer = ByteBuffer.allocate(FileCopyUtils.BUFFER_SIZE);
		this.fileChannel.read(buffer, position, buffer, this); // <3>
		while (this.bytesRead > 0) {
			this.position = this.position + this.bytesRead;
			this.fileChannel.read(buffer, this.position, buffer, this);
		}
	}

	@Override
	public void completed(Integer result, ByteBuffer buffer) {

		this.bytesRead = result; // <4>

		if (this.bytesRead < 0)	{
			return;
		}

		buffer.flip();

		byte[] data = new byte[buffer.limit()];
		buffer.get(data);

		// <5>
		consumer.accept(BytesPayload.from(data, data.length));

		buffer.clear();

		this.position = this.position + this.bytesRead;
		this.fileChannel.read(buffer, this.position, buffer, this);
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		log.error(exc);
	}
}
