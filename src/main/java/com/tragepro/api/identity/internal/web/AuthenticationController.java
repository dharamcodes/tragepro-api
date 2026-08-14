package com.tragepro.api.identity.internal.web;

import com.tragepro.api.identity.IdentityAdapter;
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

  private final IdentityAdapter identityAdapter;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok().body(identityAdapter.loginUser(loginRequest));
  }

  @PostMapping("/signup")
  public ResponseEntity<AuthenticationResponse> signup(
      @Valid @RequestBody AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok().body(identityAdapter.signup(authenticationRequest));
  }

  @GetMapping("/find/{userName}")
  public ResponseEntity<AuthenticationResponse> get(@PathVariable String userName) {
    return ResponseEntity.ok().body(identityAdapter.getByUserName(userName));
  }

  @PutMapping("/update/{userName}")
  public ResponseEntity<AuthenticationResponse> update(
      @PathVariable String userName,
      @Valid @RequestBody AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok()
        .body(identityAdapter.updateAuthenticationDetails(userName, authenticationRequest));
  }

  @DeleteMapping("/deactivate/{userName}")
  public ResponseEntity<Void> deactivate(@PathVariable String userName) {
    identityAdapter.deactivateAuthentication(userName);
    return ResponseEntity.accepted().build();
  }

  @PutMapping("/password")
  public ResponseEntity<Void> changePassword(
      @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    identityAdapter.changePassword(resetPasswordRequest);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/reset-password/{userName}")
  public ResponseEntity<Void> resetPassword(@PathVariable String userName) {
    identityAdapter.resetPassword(userName);
    return ResponseEntity.accepted().build();
  }

  @DeleteMapping("/delete/{userName}")
  public ResponseEntity<Void> delete(@PathVariable String userName) {
    identityAdapter.deleteAuthentication(userName);
    return ResponseEntity.accepted().build();
  }
}
