package com.tragepro.api.identity.web;

import com.tragepro.api.domain.identity.request.AccountDetailRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;
import com.tragepro.api.identity.adapter.AccountDetailAdapter;
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

  private final AccountDetailAdapter accountDetailAdapter;

  @PostMapping
  public ResponseEntity<AccountDetailResponse> create(
      @Valid @RequestBody AccountDetailRequest accountDetailRequest) {
    return ResponseEntity.ok().body(accountDetailAdapter.createAccount(accountDetailRequest));
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<AccountDetailResponse> get(@PathVariable String identifier) {
    return ResponseEntity.ok().body(accountDetailAdapter.getAccount(identifier));
  }

  @PutMapping("/{identifier}")
  public ResponseEntity<AccountDetailResponse> update(
      @PathVariable String identifier,
      @Valid @RequestBody AccountDetailRequest accountDetailRequest) {
    return ResponseEntity.ok()
        .body(accountDetailAdapter.updateAccountDetails(identifier, accountDetailRequest));
  }

  @DeleteMapping("/{identifier}")
  public ResponseEntity<Void> deactivate(@PathVariable String identifier) {
    accountDetailAdapter.deactivateAccount(identifier);
    return ResponseEntity.ok().build();
  }
}
