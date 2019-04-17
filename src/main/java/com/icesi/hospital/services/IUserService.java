package com.icesi.hospital.services;

import com.icesi.hospital.model.User;

/**
 * IUserService
 */
public interface IUserService {

    public User addUser(User user);
	
	public User removeAttention(String login);
	
    public User updateUser(User user);
    
    public User getUser(String login);

    public boolean validateUser(User user);

    
}