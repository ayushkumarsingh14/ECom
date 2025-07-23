package com.ayush.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.backend.model.User;
import com.ayush.backend.repo.UserRepo;
import com.ayush.backend.service.JwtService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired private UserRepo repo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private AuthenticationManager manager;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> req) {
        User user = new User(null,
                req.get("username"),
                encoder.encode(req.get("password")),
                req.getOrDefault("role", "USER"), 
                Boolean.parseBoolean(req.getOrDefault("isEnable", "true"))
        );
        repo.save(user);
        return ResponseEntity.ok("User Registered Successfully");
    }

    @PostMapping("/login")
public ResponseEntity<String> login(@RequestBody Map<String, String> req) {
    try {
        System.out.println("➡️ Login request for: " + req.get("username"));
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.get("username"),
                        req.get("password")
                )
        );
        System.out.println("✅ Login success for: " + auth.getName());

        String token = jwtService.generateToken((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(token);

    } catch (Exception e) {
        System.out.println("❌ LOGIN FAILED: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        return ResponseEntity.status(403).body("Login failed: " + e.getMessage());
    }
}
}
