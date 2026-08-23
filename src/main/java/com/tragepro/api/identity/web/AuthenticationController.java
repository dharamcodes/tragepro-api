package com.tragepro.api.identity.web;

import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import com.tragepro.api.domain.identity.request.LoginRequest;
import com.tragepro.api.domain.identity.request.ResetPasswordRequest;
import com.tragepro.api.domain.identity.response.AuthenticationResponse;
import com.tragepro.api.domain.identity.response.LoginResponse;
import com.tragepro.api.identity.service.AuthenticationService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "1. Identity & Access Management",
        description = "User authentication, registration, password lifecycle, and credentials")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @RateLimiter(name = "authLimiter")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authenticationService.login(loginRequest));
    }

    @RateLimiter(name = "authLimiter")
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok().body(authenticationService.signup(authenticationRequest));
    }

    @GetMapping("/csrf")
    public ResponseEntity<org.springframework.security.web.csrf.CsrfToken> getCsrfToken(
            org.springframework.security.web.csrf.CsrfToken csrfToken) {
        return ResponseEntity.ok().body(csrfToken);
    }

    @PreAuthorize("hasRole('APP_ADMIN') or #userName == authentication.name")
    @GetMapping("/find/{userName}")
    public ResponseEntity<AuthenticationResponse> get(@PathVariable String userName) {
        return ResponseEntity.ok().body(authenticationService.getByUserName(userName));
    }

    @PreAuthorize("hasRole('APP_ADMIN') or #userName == authentication.name")
    @PutMapping("/update/{userName}")
    public ResponseEntity<AuthenticationResponse> update(
            @PathVariable String userName, @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok()
                .body(authenticationService.updateAuthenticationDetails(userName, authenticationRequest));
    }

    @PreAuthorize("hasRole('APP_ADMIN') or #userName == authentication.name")
    @DeleteMapping("/deactivate/{userName}")
    public ResponseEntity<Void> deactivate(@PathVariable String userName) {
        authenticationService.deactivateAuthentication(userName);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasRole('APP_ADMIN') or #resetPasswordRequest.userName() == authentication.name")
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        authenticationService.changePassword(resetPasswordRequest);
        return ResponseEntity.accepted().build();
    }

    @RateLimiter(name = "authLimiter")
    @PostMapping("/reset-password/{userName}")
    public ResponseEntity<Void> resetPassword(@PathVariable String userName) {
        authenticationService.resetPassword(userName);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasRole('APP_ADMIN') or #userName == authentication.name")
    @DeleteMapping("/delete/{userName}")
    public ResponseEntity<Void> delete(@PathVariable String userName) {
        authenticationService.deleteAuthentication(userName);
        return ResponseEntity.accepted().build();
    }
}
