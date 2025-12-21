package com.ssafy.gt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;
    private final long refreshExpirationTime;

    public JwtUtil(
            @Value("${jwt.secret:MySecretKeyForJWTTokenGenerationAndValidationPurpose}") String secret,
            @Value("${jwt.expiration:86400000}") long expirationTime,
            @Value("${jwt.refresh-expiration:604800000}") long refreshExpirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    /**
     * JWT 토큰 생성
     */
    public String generateToken(int id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claim("id", id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * JWT 토큰에서 userId 추출
     */
    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("id", Integer.class);
    }

    /**
     * JWT 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 토큰 만료 시 예외를 던져 필터에서 401을 반환
            throw e;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(Integer id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .claim("id", id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Refresh Token 만료 시간 반환 (Date 객체)
     */
    public Date getRefreshTokenExpiryDate() {
        return new Date(System.currentTimeMillis() + refreshExpirationTime);
    }

    /**
     * Token에서 만료 시간 추출
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }
}
