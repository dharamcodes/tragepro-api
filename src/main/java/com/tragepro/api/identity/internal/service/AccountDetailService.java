package com.tragepro.api.identity.internal.service;

import com.tragepro.api.identity.dto.AccountDetailRequest;
import com.tragepro.api.identity.dto.AccountDetailResponse;

/** Service interface managing user account profile details. */
public interface AccountDetailService {

  /**
   * Creates a new user account detail record.
   *
   * @param accountDetailRequest payload containing account details
   * @return response representing created account details
   */
  AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

  /**
   * Retrieves account details by identifier.
   *
   * @param identifier account identifier
   * @return response containing account details
   */
  AccountDetailResponse getAccount(String identifier);

  /**
   * Updates existing account details by identifier.
   *
   * @param identifier account identifier
   * @param accountDetailRequest payload containing updated details
   * @return response representing updated account details
   */
  AccountDetailResponse updateAccountDetails(
      String identifier, AccountDetailRequest accountDetailRequest);

  /**
   * Deactivates an account by identifier.
   *
   * @param identifier account identifier
   */
  void deactivateAccount(String identifier);
}
