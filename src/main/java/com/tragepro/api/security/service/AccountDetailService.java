package io.tragepro.api.application.service;

import io.tragepro.api.security.model.request.AccountDetailRequest;
import io.tragepro.api.security.model.response.AccountDetailResponse;

public interface AccountDetailService {
    AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

    AccountDetailResponse getAccount(String identifier);

    AccountDetailResponse updateAccountDetails(String identifier, AccountDetailRequest accountDetailRequest);

    void deactivateAccount(String identiFier);
}
