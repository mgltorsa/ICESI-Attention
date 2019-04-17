package com.icesi.hospital.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Supply {

	@NonNull
	@Id
	@NotBlank(message = "{field.error}")
	private String consecutive;

	@NonNull
	@ManyToOne
	@NotNull(message="{field.error}")
	private Medicine medicine;

	@Min(value = 1, message = "{quantity.error}" )
	private int quantity;

	@NonNull
	@ManyToOne
	@NotNull(message="{field.error}")
	private Patient patient;

	@NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")  
	@Past
	@NotNull(message="{date.error}")
	private Date date;

	private String observations;

	@NonNull
	@NotBlank(message = "{field.error}")
	private String pathology;

}
