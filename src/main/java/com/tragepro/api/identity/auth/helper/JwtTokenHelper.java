package com.tragepro.api.identity.auth.helper;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class JwtTokenHelper {

    private final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode("UVVETsBqGWkYVZrM+VWTEMPn/aHp+HLjJL8hQlFyytQ="));
    private final long EXPIRY_MINUTES = 7200;
    private final long EXPIRY_MINUTES_RESET_PASSWORD = 15;

    public String generateToken(String userName, Map<String, String> claims) {
        Instant now = Instant.now();
        Instant expiration = now.plus(EXPIRY_MINUTES, ChronoUnit.MINUTES);
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(now))
                .claims(claims)
                .expiration(Date.from(expiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateResetPasswordToken(String userName, Map<String, String> claims) {
        Instant now = Instant.now();
        Instant expiration = now.plus(EXPIRY_MINUTES_RESET_PASSWORD, ChronoUnit.MINUTES);
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(now))
                .claims(claims)
                .expiration(Date.from(expiration))
                .signWith(SECRET_KEY)
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
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
    }

    private boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }
}
