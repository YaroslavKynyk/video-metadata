package com.goldmediatech.vms.configuration;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 3600000; // 1 hour

    public String generateToken(Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var now = new Date();
        var expiryDate = new Date(now.getTime() + expirationTime);

        var claims = Map.of(
                "sub", userDetails.getUsername(),
                "roles", roles,
                "iat", now.getTime(),
                "exp", expiryDate.getTime()
        );

        return Jwts.builder()
                .claims().add(claims)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String extractUserName(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("[JWT] Invalid token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error("[JWT] Expired token: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("[JWT] Unsupported token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("[JWT] Parsing error: {}", e.getMessage());
            return false;
        }
    }
}
