package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.RequestPredicate;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

public class CaseInsensitiveRequestPredicateTest {

		@Test
		public void normalConfigurationFails() {

				MockServerRequest request = MockServerRequest
					.builder()
					.uri(URI.create("/CATS"))
					.build();

				RequestPredicate predicate = GET("/cats");

				Assertions.assertThat(predicate.test(request)).isFalse();
		}

		@Test
		public void ciLowercasePredicateUppercaseRequest() {
				RequestPredicate predicate = new CaseInsensitiveRequestPredicate(GET("/cats"));
				MockServerRequest request = MockServerRequest.builder().uri(URI.create("/CATS")).build();
				Assertions.assertThat(predicate.test(request)).isTrue();
		}
}