package com.icesi.hospital.repositories;

import java.util.Date;
import java.util.List;

import com.icesi.hospital.model.UrgencyAttention;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUrgencyRepository extends CrudRepository<UrgencyAttention, String> {


    List<UrgencyAttention> findByDate(Date date);

}
