package com.tragepro.api.core.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.config.JwtTokenHelper;
import com.tragepro.api.common.filter.JWTAuthFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JWTAuthFilterTest {

  @Mock private UserDetailsService userDetailsService;

  @Mock private FilterChain filterChain;

  private JWTAuthFilter jwtAuthFilter;

  private JwtTokenHelper jwtTokenHelper;

  @BeforeEach
  void setUp() {
    jwtTokenHelper = new JwtTokenHelper("UVVETsBqGWkYVZrM+VWTEMPn/aHp+HLjJL8hQlFyytQ=", 7200, 15);
    jwtAuthFilter = new JWTAuthFilter(userDetailsService, jwtTokenHelper);
    SecurityContextHolder.clearContext();
  }

  @Test
  void testJWTAuthFilterPublicEndpoints() throws Exception {
    String[] publicEndpoints = {
      "/swagger-ui/index.html",
      "/swagger-ui.html",
      "/v3/api-docs",
      "/swagger-resources/configuration/ui",
      "/webjars/springfox-swagger-ui/springfox.css",
      "/api/v1/auth/login",
      "/api/v1/auth/signup"
    };

    for (String endpoint : publicEndpoints) {
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI(endpoint);
      MockHttpServletResponse response = new MockHttpServletResponse();
      FilterChain mockChain = mock(FilterChain.class);

      jwtAuthFilter.doFilter(request, response, mockChain);
      verify(mockChain).doFilter(request, response);
    }
  }

  @Test
  void testJWTAuthFilterMissingOrInvalidHeader() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/v1/candles");
    MockHttpServletResponse response = new MockHttpServletResponse();

    jwtAuthFilter.doFilter(request, response, filterChain);
    verify(filterChain, times(1)).doFilter(request, response);

    request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
    jwtAuthFilter.doFilter(request, response, filterChain);
    verify(filterChain, times(2)).doFilter(request, response);
  }

  @Test
  void testJWTAuthFilterValidTokenRoles() throws Exception {
    String[] roles = {"APP_USER", "APP_MANAGER", "APP_ADMIN", "SUPER_USER"};

    for (String role : roles) {
      SecurityContextHolder.clearContext();
      String token = jwtTokenHelper.generateToken("testUser", Map.of("role", role));

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI("/api/v1/candles");
      request.addHeader("Authorization", "Bearer " + token);
      MockHttpServletResponse response = new MockHttpServletResponse();
      FilterChain mockChain = mock(FilterChain.class);

      UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
      when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

      jwtAuthFilter.doFilter(request, response, mockChain);
      verify(mockChain).doFilter(request, response);
      assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
  }

  @Test
  void testJWTAuthFilterResetPasswordSuccess() throws Exception {
    String token =
        jwtTokenHelper.generateResetPasswordToken(
            "testUser",
            Map.of(
                "role", "APP_USER",
                "passwordReset", "PASSWORD_RESET_CLAIM"));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/v1/reset-password");
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain mockChain = mock(FilterChain.class);

    UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
    when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

    jwtAuthFilter.doFilter(request, response, mockChain);
    verify(mockChain).doFilter(request, response);
  }

  @Test
  void testJWTAuthFilterResetPasswordDenied() throws Exception {
    String token =
        jwtTokenHelper.generateResetPasswordToken(
            "testUser", Map.of("role", "APP_USER", "passwordReset", "INVALID_CLAIM_VALUE"));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/v1/reset-password");
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();

    UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
    when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

    jwtAuthFilter.doFilter(request, response, filterChain);
    assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    assertTrue(response.getContentAsString().contains("Invalid or expired token"));
  }

  @Test
  void testJWTAuthFilterInvalidTokenException() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/v1/candles");
    request.addHeader("Authorization", "Bearer invalidtokenhere");
    MockHttpServletResponse response = new MockHttpServletResponse();

    jwtAuthFilter.doFilter(request, response, filterChain);
    assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
  }
}
