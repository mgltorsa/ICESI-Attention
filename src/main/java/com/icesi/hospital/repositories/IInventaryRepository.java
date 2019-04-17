package com.icesi.hospital.repositories;

import java.util.List;

import com.icesi.hospital.model.InventaryMedicine;
import com.icesi.hospital.model.Medicine;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IInventaryRepository extends CrudRepository<InventaryMedicine, Long> {


    List<InventaryMedicine> findByMedicine(Medicine medicine);
}
