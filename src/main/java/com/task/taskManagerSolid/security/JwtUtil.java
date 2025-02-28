package com.task.taskManagerSolid.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {


    private static final String SECRET_KEY = "00000000000000000000000000000000";

    private static final Key SECRET = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET_KEY.getBytes()));

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(SECRET, SignatureAlgorithm.HS256)
                .compact();

    }


    public String extractUsername(String token) {
        return getClaims(token).getSubject();

    }

    public Claims getClaims(String token) {
        return Jwts.parser().build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
