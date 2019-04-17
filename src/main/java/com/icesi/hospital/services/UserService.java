package com.icesi.hospital.services;

import java.util.Optional;

import javax.annotation.PostConstruct;

import com.icesi.hospital.model.MyUserPrincipal;
import com.icesi.hospital.model.State;
import com.icesi.hospital.model.User;
import com.icesi.hospital.repositories.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    private void setupUsers() {

        User user = new User();
        user.setLogin("Rgn");
        user.setName("Raegan");
        user.setLastNames("Reichel");
        user.setPassword( encoder.encode("123") );
        user.setState(State.ACTIVO);
        addUser(user);

        User user2 = new User();
        user2.setLogin("admin");
        user2.setName("Miguel");
        user2.setLastNames("Torres");
        
        
        

        user2.setPassword( encoder.encode("123") );
        user2.setState(State.ACTIVO);
        addUser(user2);
    }


    @Override
    public User addUser(User user) {

        User resUser = null;
        if (validateUser(user)) {

            Optional<User> opt = userRepository.findById(user.getLogin());
            if (opt.isPresent()) {
                throw new IllegalArgumentException("user already exist");
            }

            userRepository.save(user);
        }

        return resUser;
    }

    @Override
    public User removeAttention(String login) {

        Optional<User> opt = userRepository.findById(login);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("user doesn't exist");
        }

        User removed = opt.get();
        userRepository.delete(removed);
        return removed;
    }

    @Override
    public User updateUser(User user) {

        User resUser = null;
        if (validateUser(user)) {
            Optional<User> opt = userRepository.findById(user.getLogin());
            if (!opt.isPresent()) {
                throw new IllegalArgumentException("user doesn't exist");
            }
            resUser = opt.get();

            userRepository.save(user);

        }

        return resUser;
    }

    @Override
    public User getUser(String login) {

        Optional<User> opt = userRepository.findById(login);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("user doesn't exist");
        }

        return opt.get();
    }

    @Override
    public boolean validateUser(User user) {
        // TODO:
        return true;
    }

  

   
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(username);
       if (!user.isPresent()) {
           throw new RuntimeException(username);
       }
       return new MyUserPrincipal(user.get());
    }

    

    

}