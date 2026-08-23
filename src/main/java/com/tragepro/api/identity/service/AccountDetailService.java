package com.tragepro.api.identity.service;

import com.tragepro.api.domain.identity.request.AccountDetailRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;

public interface AccountDetailService {
    AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

    AccountDetailResponse getAccount(String identifier);

    AccountDetailResponse updateAccountDetails(String identifier, AccountDetailRequest accountDetailRequest);

    void deactivateAccount(String identiFier);
}
