package com.tragepro.api.common.config;

import io.jsonwebtoken.Claims;
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

  private final SecretKey secretKey;
  private final long expiryMinutes;
  private final long resetPasswordExpiryMinutes;

  public JwtTokenHelper(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expirationMinutes:7200}") long expiryMinutes,
      @Value("${jwt.resetPasswordExpirationMinutes:15}") long resetPasswordExpiryMinutes) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expiryMinutes = expiryMinutes;
    this.resetPasswordExpiryMinutes = resetPasswordExpiryMinutes;
  }

  public String generateToken(String userName, Map<String, String> claims) {
    Instant now = Instant.now();
    Instant expiration = now.plus(expiryMinutes, ChronoUnit.MINUTES);
    return Jwts.builder()
        .claims(claims)
        .subject(userName)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .signWith(secretKey)
        .compact();
  }

  public String generateResetPasswordToken(String userName, Map<String, String> claims) {
    Instant now = Instant.now();
    Instant expiration = now.plus(resetPasswordExpiryMinutes, ChronoUnit.MINUTES);
    return Jwts.builder()
        .claims(claims)
        .subject(userName)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .signWith(secretKey)
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
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }

  private boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }
}
