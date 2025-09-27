package com.example.banking.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Returns the signing key used to sign and validate JWTs.
     *
     * @return the HMAC SHA key derived from the configured secret
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token for the given user.
     *
     * <p>The token contains the username as the subject, roles as a claim,
     * issued time, and expiration time.</p>
     *
     * @param userDetails the authenticated user details
     * @return a signed JWT token as a string
     */
    public String generateToken(org.springframework.security.core.userdetails.User userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return the username if successfully extracted, otherwise {@code null}
     */
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            logger.debug("Failed to extract username from JWT: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validates the given JWT token.
     *
     * <p>The token is considered valid if it is correctly signed, not expired,
     * and structurally correct.</p>
     *
     * @param token the JWT token
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }
}
