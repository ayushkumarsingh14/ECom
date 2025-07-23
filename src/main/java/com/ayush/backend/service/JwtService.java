package com.ayush.backend.service;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
    private final String SECRET_KEY = "your_secure_32+_char_minimum_secret_here";

    public String generateToken(UserDetails ud) {
        return Jwts.builder()
            .subject(ud.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600_000))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), Jwts.SIG.HS256)
            .claim("roles", ud.getAuthorities())
            .compact();
    }
    
 public String extractUsername(String token){
        System.out.println("🔍 Extracting username from token");
        return Jwts.parser()
                   .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject();
    }


public boolean isTokenValid(String token , UserDetails userDetails){
         final String username = extractUsername(token);
        boolean valid = username.equals(userDetails.getUsername()) && !isExpired(token);
        System.out.println("🛡️ Token valid? " + valid);
        return valid;
    }

    public boolean isExpired(String token){
        Date exp = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        System.out.println("⌛ Token Expiration: " + exp);
        return exp.before(new Date());
    }
}
