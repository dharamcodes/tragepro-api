package com.tragepro.api.identity.internal.web;

import com.tragepro.api.identity.dto.AccountDetailRequest;
import com.tragepro.api.identity.dto.AccountDetailResponse;
import com.tragepro.api.identity.internal.service.AccountDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. AccountDetailController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountDetailController {

  private final AccountDetailService accountDetailService;

  @PostMapping
  public ResponseEntity<AccountDetailResponse> create(
      @Valid @RequestBody AccountDetailRequest accountDetailRequest) {
    return ResponseEntity.ok().body(accountDetailService.createAccount(accountDetailRequest));
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<AccountDetailResponse> get(@PathVariable String identifier) {
    return ResponseEntity.ok().body(accountDetailService.getAccount(identifier));
  }

  @PutMapping("/{identifier}")
  public ResponseEntity<AccountDetailResponse> update(
      @PathVariable String identifier,
      @Valid @RequestBody AccountDetailRequest accountDetailRequest) {
    return ResponseEntity.ok()
        .body(accountDetailService.updateAccountDetails(identifier, accountDetailRequest));
  }

  @DeleteMapping("/{identifier}")
  public ResponseEntity<Void> deactivate(@PathVariable String identifier) {
    accountDetailService.deactivateAccount(identifier);
    return ResponseEntity.ok().build();
  }
}
