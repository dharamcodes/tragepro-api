package com.tragepro.api.security.helper;

import com.tragepro.api.exception.AppException;
import com.tragepro.api.exception.constant.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry-minutes:7200}")
    private long expiryMinutes;

    @Value("${jwt.reset-expiry-minutes:15}")
    private long resetExpiryMinutes;

    public String generateToken(String userName, Map<String, String> claims) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(Instant.now()))
                .claims(claims)
                .expiration(Date.from(Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES)))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateResetPasswordToken(String userName, Map<String, String> claims) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(Instant.now()))
                .claims(claims)
                .expiration(Date.from(Instant.now().plus(resetExpiryMinutes, ChronoUnit.MINUTES)))
                .signWith(getSecretKey())
                .compact();
    }

    public String extractUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public Claims getTokenBody(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorType.SESSION_EXPIRED);
        } catch (JwtException e) {
            throw new AppException(ErrorType.INVALID_TOKEN);
        }
    }

    private boolean isTokenExpired(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
