package com.example.io;

import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

class Io {

	private final Synchronous synchronous = new Synchronous();

	public void synchronousRead(File f, Consumer<BytesPayload> consumer) {
		try {
			this.synchronous.read(f, consumer);
		}
		catch (IOException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
	}

	public void asynchronousRead(File f, Consumer<BytesPayload> consumer) {
		try {
			Asynchronous asynchronous = new Asynchronous();
			asynchronous.read(f, consumer);
		}
		catch (Exception ex) {
			ReflectionUtils.rethrowRuntimeException(ex);
		}
	}
}
