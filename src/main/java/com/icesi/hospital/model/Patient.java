package com.icesi.hospital.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Patient {

	@NonNull
	@Id
	private String document;

	@NonNull
	private String names;

	@NonNull
	private String lastnames;

	private String academicProgram;

	private String academicDependency;

	@NonNull
	private State state;

}
