package com.nck.taxi.security;

import io.jsonwebtoken.*;
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
    private long expiration;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /* ════════════════════════════════
       GÉNÉRATION
    ════════════════════════════════ */

    /** Token driver (sans role) */
    public String generateToken(String username, String driverId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("driverId", driverId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Token driver (avec role) */
    public String generateToken(String username, String driverId, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("driverId", driverId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Token B2B entreprise (7 jours) */
    public String generateB2BToken(String email, Long companyId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("companyId", companyId)
                .claim("role", "B2B")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()
                        + 1000L * 60 * 60 * 24 * 7))
                .signWith(getKey(), SignatureAlgorithm.HS256)  // ← getKey() pas getSignKey()
                .compact();
    }

    /* ════════════════════════════════
       EXTRACTION
    ════════════════════════════════ */

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractDriverId(String token) {
        return getClaims(token).get("driverId", String.class);
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Long extractCompanyId(String token) {
        return getClaims(token).get("companyId", Long.class);
    }

    public String extractB2BEmail(String token) {
        return getClaims(token).getSubject();  // ← simplifié, utilise getClaims()
    }

    /* ════════════════════════════════
       VALIDATION
    ════════════════════════════════ */

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /* ════════════════════════════════
       INTERNE
    ════════════════════════════════ */

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}