package com.tragepro.api.identity.adapter;

import com.tragepro.api.domain.identity.request.AccountDetailRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;

public interface AccountDetailAdapter {
  AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

  AccountDetailResponse getAccount(String identifier);

  AccountDetailResponse updateAccountDetails(
      String identifier, AccountDetailRequest accountDetailRequest);

  void deactivateAccount(String identifier);
}
