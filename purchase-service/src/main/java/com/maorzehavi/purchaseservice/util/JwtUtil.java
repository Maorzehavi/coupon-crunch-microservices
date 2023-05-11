package com.maorzehavi.purchaseservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${api.key}")
    private String apiKey;

    public void validateUser(Long userId, HttpServletRequest request) {
        String apiKeyHeader = request.getHeader("x-api-key");
        if (apiKeyHeader != null && apiKeyHeader.equals(apiKey)) return;
        String token = request.getHeader("Authorization").substring(7);
        String clientType = extractClaim(token, "clientType").toString();
        if (clientType.contains("ADMIN")) return;
        Long tokenUserId =  Long.parseLong(extractClaim(token, "userId").toString());
        if (!userId.equals(tokenUserId)) {
            throw new RuntimeException("User id in token does not match user id in request");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Object extractClaim(String token, String claimName) {
        return extractClaim(token, claims -> claims.get(claimName));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long extractUserId(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        return Long.parseLong(extractClaim(token, "userId").toString());
    }
}