package com.example.demo;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;


@RestController // <1>
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)  // <2>
@org.springframework.context.annotation.Profile("classic")
class ProfileRestController {

	private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
	private final ProfileService profileRepository;

	ProfileRestController(ProfileService profileRepository) {
		this.profileRepository = profileRepository;
	}

	//<3>
	@GetMapping
	Publisher<Profile> getAll() {
		return this.profileRepository.all();
	}

	//<4>
	@GetMapping("/{id}")
	Publisher<Profile> getById(@PathVariable("id") String id) {
		return this.profileRepository.get(id);
	}

	// <5>
	@PostMapping
	Publisher<ResponseEntity<Profile>> create(@RequestBody Profile profile) {
		return this.profileRepository
			.create(profile.getEmail())
			.map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
				.contentType(mediaType)
				.build());
	}

	@DeleteMapping("/{id}")
	Publisher<Profile> deleteById(@PathVariable String id) {
		return this.profileRepository.delete(id);
	}

	@PutMapping("/{id}")
	Publisher<ResponseEntity<Profile>> updateById(@PathVariable String id, @RequestBody Profile profile) {
		return Mono
			.just(profile)
			.flatMap(p -> this.profileRepository.update(id, p.getEmail()))
			.map(p -> ResponseEntity
				.ok()
				.contentType(this.mediaType)
				.build());
	}
}
