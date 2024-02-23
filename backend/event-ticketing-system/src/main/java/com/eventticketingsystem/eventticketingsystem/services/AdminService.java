package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    public User saveUser(User user){
        return userRepository.save(user);
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}
