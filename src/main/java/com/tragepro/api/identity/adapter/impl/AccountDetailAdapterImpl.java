package com.tragepro.api.identity.adapter.impl;

import com.tragepro.api.domain.identity.request.AccountDetailRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;
import com.tragepro.api.identity.adapter.AccountDetailAdapter;
import com.tragepro.api.identity.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDetailAdapterImpl implements AccountDetailAdapter {
  private final AccountDetailService accountDetailService;

  @Override
  public AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest) {
    return accountDetailService.createAccount(accountDetailRequest);
  }

  @Override
  public AccountDetailResponse getAccount(String identifier) {
    return accountDetailService.getAccount(identifier);
  }

  @Override
  public AccountDetailResponse updateAccountDetails(
      String identifier, AccountDetailRequest accountDetailRequest) {
    return accountDetailService.updateAccountDetails(identifier, accountDetailRequest);
  }

  @Override
  public void deactivateAccount(String identifier) {
    accountDetailService.deactivateAccount(identifier);
  }
}
