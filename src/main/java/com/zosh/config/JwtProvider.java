package com.zosh.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {
        // --- THIS IS THE FIX ---
        // 1. Get the user's roles (authorities)
        String authorities = populateAuthorities(auth.getAuthorities());

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 864000000)) // ~10 days
                .claim("email", auth.getName())
                // 2. Add the authorities to the token
                .claim("authorities", authorities)
                .signWith(key)
                .compact();

        return jwt;
    }

    public String getEMailFromToken(String jwt) {
        jwt = jwt.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return String.valueOf(claims.get("email"));
    }

    // This helper method converts the user's roles into a comma-separated string
    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        return collection.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}