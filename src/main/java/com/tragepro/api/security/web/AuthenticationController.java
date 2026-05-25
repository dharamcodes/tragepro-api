package com.tragepro.api.security.web;

import com.tragepro.api.security.model.request.AuthenticationRequest;
import com.tragepro.api.security.model.request.LoginRequest;
import com.tragepro.api.security.model.request.ResetPasswordRequest;
import com.tragepro.api.security.model.response.AuthenticationResponse;
import com.tragepro.api.security.model.response.LoginResponse;
import com.tragepro.api.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User registration, login, profile, and password management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/config/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Login", description = "Authenticate with username and password. Returns a signed JWT token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful — JWT token returned"),
        @ApiResponse(responseCode = "403", description = "Invalid credentials"),
        @ApiResponse(responseCode = "404", description = "User not found or inactive")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authenticationService.login(loginRequest));
    }

    @Operation(summary = "Signup", description = "Register a new user. userName defaults to email if omitted.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok().body(authenticationService.signup(authenticationRequest));
    }

    @Operation(summary = "Get user by username", description = "Fetch authentication details for a specific user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/find/{userName}")
    public ResponseEntity<AuthenticationResponse> get(
            @Parameter(description = "Unique username to look up", required = true) @PathVariable String userName) {
        return ResponseEntity.ok().body(authenticationService.getByUserName(userName));
    }

    @Operation(
            summary = "Update auth details",
            description = "Update a user's authentication record. Only provided fields are applied.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update/{userName}")
    public ResponseEntity<AuthenticationResponse> update(
            @Parameter(description = "Username of the record to update", required = true) @PathVariable String userName,
            @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok()
                .body(authenticationService.updateAuthenticationDetails(userName, authenticationRequest));
    }

    @Operation(summary = "Deactivate user", description = "Soft-delete a user by setting isActive = false.")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "Account deactivated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "User not found or already inactive")
    })
    @DeleteMapping("/deactivate/{userName}")
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "Username of the account to deactivate", required = true) @PathVariable
                    String userName) {
        authenticationService.deactivateAuthentication(userName);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Change password",
            description = "Change a user's password. Password and confirmPassword must match.")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Password and confirmPassword do not match"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "User not found or inactive")
    })
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        authenticationService.changePassword(resetPasswordRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Reset password", description = "Trigger a password-reset email with a 15-minute reset token.")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "Password reset email dispatched"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "User not found or inactive")
    })
    @PostMapping("/reset-password/{userName}")
    public ResponseEntity<Void> resetPassword(
            @Parameter(description = "Username of the account to reset", required = true) @PathVariable
                    String userName) {
        authenticationService.resetPassword(userName);
        return ResponseEntity.accepted().build();
    }
}
