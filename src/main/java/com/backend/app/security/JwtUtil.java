package com.backend.app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    @Value("${jwt.expiration}")
    private long accessTokenExpirationMillis;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpirationMillis;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        System.out.println("JWT secret key initialized.");
    }

    // Generate access token with email and role
    public String generateAccessToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        if (role != null) {
            claims.put("role", role);
        }
        return buildToken(email, claims, accessTokenExpirationMillis);
    }

    // Generate refresh token
    public String generateRefreshToken(String email) {
        return buildToken(email, new HashMap<>(), refreshTokenExpirationMillis);
    }

    // Generic token builder
    private String buildToken(String subject, Map<String, Object> claims, long validityMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    // Parse token
    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    // Extract email (subject)
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract role
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Generic extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token).getBody();
        return claimsResolver.apply(claims);
    }

    // Extract expiration date
    public Date getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Accessor for access token expiration config
    public long getAccessExpiration() {
        return accessTokenExpirationMillis;
    }

    // Accessor for refresh token expiration config
    public long getRefreshExpiration() {
        return refreshTokenExpirationMillis;
    }

    // Optional: fallback generateToken method if called without role
    public String generateToken(String email) {
        return generateAccessToken(email, null);
    }

    // Optional: extract issued at
    public Date getIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }
}
