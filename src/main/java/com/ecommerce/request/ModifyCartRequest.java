package com.ecommerce.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
public class ModifyCartRequest {

	@NotEmpty
	@NonNull
	@JsonProperty
	private String username;

	@NotEmpty
	@NonNull
	@JsonProperty
	private long itemId;

	@NotEmpty
	@NonNull
	@JsonProperty
	private int quantity;

}