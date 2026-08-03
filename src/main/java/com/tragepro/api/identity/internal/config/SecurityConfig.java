package com.tragepro.api.identity.internal.config;

import com.tragepro.api.identity.internal.filter.JWTAuthFilter;
import com.tragepro.api.identity.internal.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailService userDetailService;

  @Bean
  public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public FilterRegistrationBean<JWTAuthFilter> jwtFilterRegistration(JWTAuthFilter filter) {
    FilterRegistrationBean<JWTAuthFilter> registration = new FilterRegistrationBean<>(filter);
    registration.setEnabled(false);
    return registration;
  }
}
