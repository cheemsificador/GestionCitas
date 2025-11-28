package com.idat.Requerimiento_01.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expirationMs;

    private Key getSigningKey() {
        // Crea una clave segura a partir del string del application.properties
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generar token con username e idUsuario
    public String generateToken(String username, Long idUsuario) {
        return Jwts.builder()
                .claim("idUsuario", idUsuario)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer username
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Extraer idUsuario
    public Long getIdUsuario(String token) {
        return getClaims(token).get("idUsuario", Long.class);
    }

    // Validar token
    public boolean isTokenValid(String token) {
        try {
            return !getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


