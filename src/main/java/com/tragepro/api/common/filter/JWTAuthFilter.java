package com.tragepro.api.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.common.config.JwtTokenHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;
  private static final String CHARACTER_ENCODING = "UTF-8";
  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer ";
  private static final String MESSAGE = "message";
  private static final String TIMESTAMP = "timestamp";
  private static final String URL = "url";
  private static final String ROLE = "role";
  private static final String ROLE_PREFIX = "ROLE_";
  private static final String PASSWORD_RESET_CLAIM = "passwordReset";
  private static final String PASSWORD_RESET_VALUE = "PASSWORD_RESET_CLAIM";

  private final UserDetailsService userDetailsService;
  private final JwtTokenHelper jwtTokenHelper;

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    return isPublicEndpoint(request.getRequestURI());
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String uri = request.getRequestURI();
      if (isPublicEndpoint(uri)) {
        filterChain.doFilter(request, response);
        return;
      }
      String authHeader = request.getHeader(AUTHORIZATION);
      if (authHeader == null || !authHeader.startsWith(BEARER)) {
        filterChain.doFilter(request, response);
        return;
      }
      String token = authHeader.substring(7);
      String username = jwtTokenHelper.extractUsername(token);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        authenticateUser(request, token, username);
      }
      var claims = jwtTokenHelper.getTokenBody(token);
      var passwordRestClaim = claims.get(PASSWORD_RESET_CLAIM, String.class);
      if (request.getRequestURI().equals("/api/v1/reset-password")
          && !PASSWORD_RESET_VALUE.equals(passwordRestClaim)) {
        sendJsonResponse(
            request, response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        return;
      }
      filterChain.doFilter(request, response);
    } catch (Exception ex) {
      sendJsonResponse(
          request, response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
    }
  }

  private static void sendJsonResponse(
      HttpServletRequest request, HttpServletResponse response, int statusCode, String message)
      throws IOException {
    response.setStatus(statusCode);
    response.setContentType(CONTENT_TYPE_JSON);
    response.setCharacterEncoding(CHARACTER_ENCODING);

    Map<String, Object> responseBody =
        Map.of(
            MESSAGE, message,
            TIMESTAMP, LocalDateTime.now().toString(),
            URL, request.getRequestURI());

    try (PrintWriter writer = response.getWriter()) {
      OBJECT_MAPPER.writeValue(writer, responseBody);
    }
  }

  private void authenticateUser(HttpServletRequest request, String token, String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    var claim = jwtTokenHelper.getTokenBody(token);
    var role = claim.get(ROLE, String.class);
    var roles =
        prepareRoles(role).stream()
            .map(r -> new SimpleGrantedAuthority(ROLE_PREFIX + r.toUpperCase()))
            .toList();
    boolean isValid = jwtTokenHelper.validateToken(token, userDetails);
    if (isValid) {
      var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, roles);
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
  }

  private List<String> prepareRoles(String role) {
    if (role == null) {
      return List.of("APP_USER");
    }
    return switch (role) {
      case "APP_MANAGER" -> List.of("APP_USER", "APP_MANAGER");
      case "APP_ADMIN" -> List.of("APP_USER", "APP_MANAGER", "APP_ADMIN");
      case "SUPER_USER" -> List.of("APP_USER", "APP_MANAGER", "APP_ADMIN", "SUPER_USER");
      default -> List.of("APP_USER");
    };
  }

  private boolean isPublicEndpoint(String uri) {
    return uri.startsWith("/swagger-ui")
        || uri.startsWith("/api-docs")
        || uri.equals("/api/v1/auth/login")
        || uri.equals("/api/v1/auth/signup");
  }
}
