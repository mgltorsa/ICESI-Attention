package com.icesi.hospital.repositories;


import com.icesi.hospital.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * IUserRepository
 */
@Repository
public interface IUserRepository extends CrudRepository<User,String>{

    
   
    
}