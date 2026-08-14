package com.tragepro.api.identity.web;

import com.tragepro.api.identity.AuthenticationService;
import com.tragepro.api.identity.model.request.AuthenticationRequest;
import com.tragepro.api.identity.model.request.LoginRequest;
import com.tragepro.api.identity.model.request.ResetPasswordRequest;
import com.tragepro.api.identity.model.response.AuthenticationResponse;
import com.tragepro.api.identity.model.response.LoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "1. AuthenticationController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok().body(authenticationService.login(loginRequest));
  }

  @PostMapping("/signup")
  public ResponseEntity<AuthenticationResponse> signup(
      @Valid @RequestBody AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok().body(authenticationService.signup(authenticationRequest));
  }

  @GetMapping("/find/{userName}")
  public ResponseEntity<AuthenticationResponse> get(@PathVariable String userName) {
    return ResponseEntity.ok().body(authenticationService.getByUserName(userName));
  }

  @PutMapping("/update/{userName}")
  public ResponseEntity<AuthenticationResponse> update(
      @PathVariable String userName,
      @Valid @RequestBody AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok()
        .body(authenticationService.updateAuthenticationDetails(userName, authenticationRequest));
  }

  @DeleteMapping("/deactivate/{userName}")
  public ResponseEntity<Void> deactivate(@PathVariable String userName) {
    authenticationService.deactivateAuthentication(userName);
    return ResponseEntity.accepted().build();
  }

  @PutMapping("/password")
  public ResponseEntity<Void> changePassword(
      @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    authenticationService.changePassword(resetPasswordRequest);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/reset-password/{userName}")
  public ResponseEntity<Void> resetPassword(@PathVariable String userName) {
    authenticationService.resetPassword(userName);
    return ResponseEntity.accepted().build();
  }

  @DeleteMapping("/delete/{userName}")
  public ResponseEntity<Void> delete(@PathVariable String userName) {
    authenticationService.deleteAuthentication(userName);
    return ResponseEntity.accepted().build();
  }
}
