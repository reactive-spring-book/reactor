package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document  // <1>
@Data // <2>
@AllArgsConstructor
@NoArgsConstructor
class Profile {

	@Id // <3>
	private String id;

	// <4>
	private String email;
}
