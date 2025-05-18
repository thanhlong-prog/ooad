package com.ooad.code.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ooad.code.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    Optional findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
}
