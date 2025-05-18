package com.ooad.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ooad.code.model.User;
import com.ooad.code.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    public User findByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if(user != null) {
            return user;
        }
        return null;
    }
    public void save(User user) {
        userRepo.save(user);
    }
}
