package com.ayush.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ayush.backend.model.User;
import com.ayush.backend.repo.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired private UserRepo repo;

    public UserDetails loadUserByUsername(String uname) {
        User u = repo.findByUsername(uname)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
            .username(u.getUsername())
            .password(u.getPassword())
            .roles(u.getRole())
            .disabled(!u.isEnable())
            .build();
    }
}
