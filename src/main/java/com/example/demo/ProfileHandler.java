package com.example.demo;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class ProfileHandler {

	// <1>
	private final ProfileService profileService;

	ProfileHandler(ProfileService profileService) {
		this.profileService = profileService;
	}

	// <2>
	Mono<ServerResponse> getById(ServerRequest r) {
		return defaultReadResponse(this.profileService.get(id(r)));
	}

	Mono<ServerResponse> all(ServerRequest r) {
		return defaultReadResponse(this.profileService.all());
	}

	Mono<ServerResponse> deleteById(ServerRequest r) {
		return defaultReadResponse(this.profileService.delete(id(r)));
	}

	Mono<ServerResponse> updateById(ServerRequest r) {
		Flux<Profile> id = r.bodyToFlux(Profile.class)
			.flatMap(p -> this.profileService.update(id(r), p.getEmail()));
		return defaultReadResponse(id);
	}

	Mono<ServerResponse> create(ServerRequest request) {
		Flux<Profile> flux = request
			.bodyToFlux(Profile.class)
			.flatMap(toWrite -> this.profileService.create(toWrite.getEmail()));
		return defaultWriteResponse(flux);
	}

	// <3>
	private static Mono<ServerResponse> defaultWriteResponse(Publisher<Profile> profiles) {
		return Mono
			.from(profiles)
			.flatMap(p -> ServerResponse
				.created(URI.create("/profiles/" + p.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.build()
			);
	}

	// <4>
	private static Mono<ServerResponse> defaultReadResponse(Publisher<Profile> profiles) {
		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body(profiles, Profile.class);
	}

	private static String id(ServerRequest r) {
		return r.pathVariable("id");
	}
}
