package com.tragepro.api.identity.auth.service;

import com.tragepro.api.identity.auth.model.request.AuthenticationRequest;
import com.tragepro.api.identity.auth.model.request.LoginRequest;
import com.tragepro.api.identity.auth.model.request.ResetPasswordRequest;
import com.tragepro.api.identity.auth.model.response.AuthenticationResponse;
import com.tragepro.api.identity.auth.model.response.LoginResponse;

public interface AuthenticationService {

  LoginResponse login(LoginRequest loginRequest);

  AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

  AuthenticationResponse getByUserName(String userName);

  AuthenticationResponse updateAuthenticationDetails(
      String userName, AuthenticationRequest authenticationRequest);

  void changePassword(ResetPasswordRequest resetPasswordRequest);

  void deactivateAuthentication(String userName);

  void resetPassword(String userName);

  void deleteAuthentication(String userName);
}
