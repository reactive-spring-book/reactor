package com.example.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface ProfileRepository
	extends ReactiveMongoRepository<Profile, String> {
}
