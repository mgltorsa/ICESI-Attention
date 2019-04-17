package com.icesi.hospital.repositories;

import com.icesi.hospital.model.Patient;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPatientRepository extends CrudRepository<Patient, String> {

	
}
