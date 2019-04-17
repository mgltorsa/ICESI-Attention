package com.icesi.hospital.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Medicine {

	@NonNull
	@Id
	private String consecutive;

	@NonNull
	private String name;

	@NonNull
	private String genericName;

	@NonNull
	private String laboratory;

	@NonNull
	private AdmininistrationType administrationType;

	@NonNull
	private String indications;

	private String contraIndications;

}
