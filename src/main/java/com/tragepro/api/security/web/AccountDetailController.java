package com.tragepro.api.security.web;

import com.tragepro.api.security.model.request.AccountDetailRequest;
import com.tragepro.api.security.model.response.AccountDetailResponse;
import com.tragepro.api.security.service.AccountDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Details", description = "Create, retrieve, update, and deactivate user account profiles")
@RestController
@RequiredArgsConstructor
@RequestMapping("/config/v1/account")
public class AccountDetailController {

    private final AccountDetailService accountDetailService;

    @Operation(
            summary = "Create account",
            description = "Create a new account profile and link it to the authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error in request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "409", description = "An active account already exists for this email")
    })
    @PostMapping
    public ResponseEntity<AccountDetailResponse> create(@Valid @RequestBody AccountDetailRequest accountDetailRequest) {
        return ResponseEntity.ok().body(accountDetailService.createAccount(accountDetailRequest));
    }

    @Operation(summary = "Get account", description = "Retrieve an account profile by its identifier.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account found"),
        @ApiResponse(responseCode = "400", description = "Identifier is blank"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{identifier}")
    public ResponseEntity<AccountDetailResponse> get(
            @Parameter(description = "Base32 account identifier", required = true) @PathVariable String identifier) {
        return ResponseEntity.ok().body(accountDetailService.getAccount(identifier));
    }

    @Operation(summary = "Update account", description = "Update an account profile. Only provided fields are applied.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error in request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/{identifier}")
    public ResponseEntity<AccountDetailResponse> update(
            @Parameter(description = "Base32 account identifier", required = true) @PathVariable String identifier,
            @Valid @RequestBody AccountDetailRequest accountDetailRequest) {
        return ResponseEntity.ok().body(accountDetailService.updateAccountDetails(identifier, accountDetailRequest));
    }

    @Operation(summary = "Deactivate account", description = "Soft-delete an account by setting isActive = false.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account deactivated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "Base32 account identifier", required = true) @PathVariable String identifier) {
        accountDetailService.deactivateAccount(identifier);
        return ResponseEntity.ok().build();
    }
}
