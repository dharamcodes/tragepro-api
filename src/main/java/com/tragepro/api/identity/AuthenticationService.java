package com.tragepro.api.identity;

import com.tragepro.api.identity.model.request.AuthenticationRequest;
import com.tragepro.api.identity.model.request.LoginRequest;
import com.tragepro.api.identity.model.request.ResetPasswordRequest;
import com.tragepro.api.identity.model.response.AuthenticationResponse;
import com.tragepro.api.identity.model.response.LoginResponse;

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
