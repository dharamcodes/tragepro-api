package com.tragepro.api.identity.internal.web;

import com.tragepro.api.identity.IdentityAdapter;
import com.tragepro.api.identity.model.request.AccountDetailRequest;
import com.tragepro.api.identity.model.response.AccountDetailResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. AccountDetailController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountDetailController {

  private final IdentityAdapter identityAdapter;

  @PostMapping
  public ResponseEntity<AccountDetailResponse> create(
      @Valid @RequestBody AccountDetailRequest accountDetailRequest) {
    return ResponseEntity.ok().body(identityAdapter.createAccount(accountDetailRequest));
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<AccountDetailResponse> get(@PathVariable String identifier) {
    return ResponseEntity.ok().body(identityAdapter.getAccountById(identifier));
  }

  @PutMapping("/{identifier}")
  public ResponseEntity<AccountDetailResponse> update(
      @PathVariable String identifier,
      @Valid @RequestBody AccountDetailRequest accountDetailRequest) {
    return ResponseEntity.ok()
        .body(identityAdapter.updateAccountDetails(identifier, accountDetailRequest));
  }

  @DeleteMapping("/{identifier}")
  public ResponseEntity<Void> deactivate(@PathVariable String identifier) {
    identityAdapter.deactivateAccount(identifier);
    return ResponseEntity.ok().build();
  }
}
