package com.tragepro.api.identity;

import com.tragepro.api.identity.model.request.AccountDetailRequest;
import com.tragepro.api.identity.model.request.AuthenticationRequest;
import com.tragepro.api.identity.model.request.LoginRequest;
import com.tragepro.api.identity.model.request.ResetPasswordRequest;
import com.tragepro.api.identity.model.response.AccountDetailResponse;
import com.tragepro.api.identity.model.response.AuthenticationResponse;
import com.tragepro.api.identity.model.response.LoginResponse;

public interface IdentityAdapter {

  AuthenticationResponse login(LoginRequest request);

  LoginResponse loginUser(LoginRequest loginRequest);

  AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

  AuthenticationResponse getByUserName(String userName);

  AuthenticationResponse updateAuthenticationDetails(
      String userName, AuthenticationRequest authenticationRequest);

  void deactivateAuthentication(String userName);

  void changePassword(ResetPasswordRequest resetPasswordRequest);

  void resetPassword(String userName);

  void deleteAuthentication(String userName);

  AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest);

  AccountDetailResponse getAccountById(String identifier);

  AccountDetailResponse updateAccountDetails(
      String identifier, AccountDetailRequest accountDetailRequest);

  void deactivateAccount(String identifier);
}
