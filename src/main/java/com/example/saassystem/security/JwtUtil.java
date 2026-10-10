package com.example.saassystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 生成与解析（jjwt 0.12 API）
 *
 * @author cxt
 * @date 2026-10-09 16:04
 */
@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expire-minutes}")
    private long expireMinutes;

    private SecretKey key() { return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }

    public String createToken(Long userId, String username, Long tenantId) {
        return Jwts.builder().subject(username)
                .claim("uid", userId)
                .claim("tid", tenantId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireMinutes * 60_000))
                .signWith(key()).compact();
    }
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
