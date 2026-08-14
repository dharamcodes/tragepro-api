package com.tragepro.api.identity.internal;

import com.tragepro.api.identity.IdentityAdapter;
import com.tragepro.api.identity.model.request.AccountDetailRequest;
import com.tragepro.api.identity.model.request.AuthenticationRequest;
import com.tragepro.api.identity.model.request.LoginRequest;
import com.tragepro.api.identity.model.request.ResetPasswordRequest;
import com.tragepro.api.identity.model.response.AccountDetailResponse;
import com.tragepro.api.identity.model.response.AuthenticationResponse;
import com.tragepro.api.identity.model.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class IdentityAdapterImpl implements IdentityAdapter {

  private final AuthenticationService authenticationService;
  private final AccountDetailService accountDetailService;

  @Override
  public AuthenticationResponse login(LoginRequest request) {
    LoginResponse loginResponse = authenticationService.login(request);
    return authenticationService.getByUserName(loginResponse.userName());
  }

  @Override
  public LoginResponse loginUser(LoginRequest loginRequest) {
    return authenticationService.login(loginRequest);
  }

  @Override
  public AuthenticationResponse signup(AuthenticationRequest authenticationRequest) {
    return authenticationService.signup(authenticationRequest);
  }

  @Override
  public AuthenticationResponse getByUserName(String userName) {
    return authenticationService.getByUserName(userName);
  }

  @Override
  public AuthenticationResponse updateAuthenticationDetails(
      String userName, AuthenticationRequest authenticationRequest) {
    return authenticationService.updateAuthenticationDetails(userName, authenticationRequest);
  }

  @Override
  public void deactivateAuthentication(String userName) {
    authenticationService.deactivateAuthentication(userName);
  }

  @Override
  public void changePassword(ResetPasswordRequest resetPasswordRequest) {
    authenticationService.changePassword(resetPasswordRequest);
  }

  @Override
  public void resetPassword(String userName) {
    authenticationService.resetPassword(userName);
  }

  @Override
  public void deleteAuthentication(String userName) {
    authenticationService.deleteAuthentication(userName);
  }

  @Override
  public AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest) {
    return accountDetailService.createAccount(accountDetailRequest);
  }

  @Override
  public AccountDetailResponse getAccountById(String identifier) {
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
