package com.icesi.hospital.repositories;

import java.util.Date;
import java.util.List;

import com.icesi.hospital.model.Supply;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupplyRepository extends CrudRepository<Supply,String> {

    List<Supply> findByDate(Date date);
	
}
