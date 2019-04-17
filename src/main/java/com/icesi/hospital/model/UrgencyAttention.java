package com.icesi.hospital.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class UrgencyAttention {

	@NonNull
	@Id
	@NotBlank(message = "{field.error}")
	private String consecutive;

	@DateTimeFormat(pattern = "yyyy-MM-dd")  
	@NotNull(message = "{date.error}")
	private Date date;

	@NonNull
	@ManyToOne
	@NotNull(message = "{field.error}")
	private Patient patient;

	@NonNull
	@NotBlank(message = "{field.error}")
	private String generalDescription;

	@NonNull
	@NotBlank(message = "{field.error}")
	private String procedure;

	@NonNull
	private Boolean forwarded;	

	private String forwardedPlace;

	@NonNull
	@NotNull
	@ManyToMany
	private List<Supply> supplies;

}
