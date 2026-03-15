package com.tragepro.api.security.web;

import com.tragepro.api.security.model.request.AuthenticationRequest;
import com.tragepro.api.security.model.request.LoginRequest;
import com.tragepro.api.security.model.request.ResetPasswordRequest;
import com.tragepro.api.security.model.response.AuthenticationResponse;
import com.tragepro.api.security.model.response.LoginResponse;
import com.tragepro.api.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authenticationService.login(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok().body(authenticationService.signup(authenticationRequest));
    }

    @GetMapping("/find/{userName}")
    public ResponseEntity<AuthenticationResponse> get(@PathVariable String userName) {
        return ResponseEntity.ok().body(authenticationService.getByUserName(userName));
    }

    @PutMapping("/update/{userName}")
    public ResponseEntity<AuthenticationResponse> update(
            @PathVariable String userName, @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok()
                .body(authenticationService.updateAuthenticationDetails(userName, authenticationRequest));
    }

    @DeleteMapping("/deactivate/{userName}")
    public ResponseEntity<Void> deactivate(@PathVariable String userName) {
        authenticationService.deactivateAuthentication(userName);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        authenticationService.changePassword(resetPasswordRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset-password/{userName}")
    public ResponseEntity<Void> resetPassword(@PathVariable String userName) {
        authenticationService.resetPassword(userName);
        return ResponseEntity.accepted().build();
    }
}
