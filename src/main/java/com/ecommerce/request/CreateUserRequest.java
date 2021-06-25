package com.ecommerce.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateUserRequest {

	@NotEmpty
	@NonNull
	@JsonProperty
	private String username;

	@NotEmpty
	@NonNull
	@JsonProperty
	private String password;

	@NotEmpty
	@NonNull
	@JsonProperty
	private String confirmPassword;
}