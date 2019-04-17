package com.icesi.hospital.services;

import com.icesi.hospital.model.Patient;

public interface IPatientService {
	
	public Patient addPatient(Patient patient);

	public Patient getPatient(String id);

	public Patient removePatient(String id);

	public Patient updatePatient(Patient patient);

}
