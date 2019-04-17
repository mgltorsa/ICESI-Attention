package com.icesi.hospital.repositories;

import com.icesi.hospital.model.Medicine;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMedicineRepository extends CrudRepository<Medicine, String>{


}
