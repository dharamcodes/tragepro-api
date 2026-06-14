package com.tragepro.api.identity.account.service;

import com.tragepro.api.identity.account.model.request.AccountDetailRequest;
import com.tragepro.api.identity.account.model.response.AccountDetailResponse;

public interface AccountDetailService {
  AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

  AccountDetailResponse getAccount(String identifier);

  AccountDetailResponse updateAccountDetails(
      String identifier, AccountDetailRequest accountDetailRequest);

  void deactivateAccount(String identiFier);
}
