package com.tragepro.api.identity;

import com.tragepro.api.identity.model.request.AccountDetailRequest;
import com.tragepro.api.identity.model.response.AccountDetailResponse;

public interface AccountDetailService {
  AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

  AccountDetailResponse getAccount(String identifier);

  AccountDetailResponse updateAccountDetails(
      String identifier, AccountDetailRequest accountDetailRequest);

  void deactivateAccount(String identiFier);
}
