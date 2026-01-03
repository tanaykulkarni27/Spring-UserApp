package com.test.demo;
import java.security.Key;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTAuth {
    @Value("${app.secret}")
    private String secret;
    private int expiry = 1000 * 60 * 60 * 2;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String value) {
        return Jwts.builder()
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() * this.expiry))
                .setSubject(value)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractVal(String token){
        return Jwts.parser()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody().getSubject();
    }

}