package com.tragepro.api.common.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtTokenHelperTest {

  private JwtTokenHelper jwtTokenHelper;

  @BeforeEach
  void setUp() {
    jwtTokenHelper = new JwtTokenHelper("UVVETsBqGWkYVZrM+VWTEMPn/aHp+HLjJL8hQlFyytQ=", 7200, 15);
  }

  @Test
  void testJwtTokenHelperLifecycleAndValidation() {
    String username = "testUser";
    Map<String, String> claims = Map.of("role", "APP_USER");

    String token = jwtTokenHelper.generateToken(username, claims);
    assertNotNull(token);
    assertEquals(username, jwtTokenHelper.extractUsername(token));

    UserDetails userDetails = new User(username, "password", new ArrayList<>());
    assertTrue(jwtTokenHelper.validateToken(token, userDetails));

    String resetToken = jwtTokenHelper.generateResetPasswordToken(username, claims);
    assertNotNull(resetToken);
    assertEquals(username, jwtTokenHelper.extractUsername(resetToken));

    String invalidToken = token + "modifiedSignature";
    assertThrows(Exception.class, () -> jwtTokenHelper.getTokenBody(invalidToken));
  }
}
