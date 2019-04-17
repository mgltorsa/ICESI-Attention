package com.icesi.hospital.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
public class User {


	@NonNull
	@Id
	@NotBlank
	private String login;

	@NonNull
	private String name;

	@NonNull
	private String lastNames;

	@NonNull
	@NotEmpty
	private String password;

	@NonNull
	private State state;
}
