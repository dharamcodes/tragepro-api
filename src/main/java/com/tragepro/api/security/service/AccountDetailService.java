package com.tragepro.api.security.service;

import com.tragepro.api.security.model.request.AccountDetailRequest;
import com.tragepro.api.security.model.response.AccountDetailResponse;

public interface AccountDetailService {

    /**
     * Creates a new account detail record and associates it with the currently
     * authenticated user by adding the generated identifier to their profile.
     *
     * @param accountDetailRequest payload containing name, email, and phone number
     * @return {@link AccountDetailResponse} with the persisted account including its identifier
     * @throws com.tragepro.api.exception.AppException DATA_EXISTS if an active account already exists for the email
     */
    AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

    /**
     * Retrieves a single account by its unique identifier.
     *
     * @param identifier the Base32 identifier generated at account creation
     * @return {@link AccountDetailResponse} for the matched account
     * @throws com.tragepro.api.exception.AppException INVALID_PARAMETER if identifier is blank
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no account matches the identifier
     */
    AccountDetailResponse getAccount(String identifier);

    /**
     * Updates mutable fields on an existing account record.
     * Uses a merge strategy — only non-null fields in the request are applied.
     *
     * @param identifier           the identifier of the account to update
     * @param accountDetailRequest fields to apply to the existing record
     * @return {@link AccountDetailResponse} reflecting the updated state
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no account matches the identifier
     */
    AccountDetailResponse updateAccountDetails(String identifier, AccountDetailRequest accountDetailRequest);

    /**
     * Soft-deletes an account by setting {@code isActive = false}.
     *
     * @param identifier the identifier of the account to deactivate
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no account matches the identifier
     */
    void deactivateAccount(String identifier);
}
