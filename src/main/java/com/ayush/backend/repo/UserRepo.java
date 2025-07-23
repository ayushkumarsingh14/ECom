package com.ayush.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.backend.model.User;

public interface UserRepo extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);    
}
