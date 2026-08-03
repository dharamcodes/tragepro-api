package com.tragepro.api.identity.internal.service;

import com.tragepro.api.identity.dto.AuthenticationRequest;
import com.tragepro.api.identity.dto.AuthenticationResponse;
import com.tragepro.api.identity.dto.LoginRequest;
import com.tragepro.api.identity.dto.LoginResponse;
import com.tragepro.api.identity.dto.ResetPasswordRequest;

/** Service interface managing user authentication, login, password reset, and signup operations. */
public interface AuthenticationService {

  /**
   * Authenticates user login credentials and generates JWT token.
   *
   * @param loginRequest login credentials
   * @return login response containing token
   */
  LoginResponse login(LoginRequest loginRequest);

  /**
   * Registers a new user account.
   *
   * @param authenticationRequest signup request payload
   * @return authentication response representing registered user
   */
  AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

  /**
   * Retrieves authentication details by username.
   *
   * @param userName username
   * @return authentication response
   */
  AuthenticationResponse getByUserName(String userName);

  /**
   * Updates authentication details by username.
   *
   * @param userName username
   * @param authenticationRequest updated payload
   * @return updated authentication response
   */
  AuthenticationResponse updateAuthenticationDetails(
      String userName, AuthenticationRequest authenticationRequest);

  /**
   * Changes user password.
   *
   * @param resetPasswordRequest request containing username and passwords
   */
  void changePassword(ResetPasswordRequest resetPasswordRequest);

  /**
   * Deactivates a user authentication account.
   *
   * @param userName username
   */
  void deactivateAuthentication(String userName);

  /**
   * Triggers a password reset token for user.
   *
   * @param userName username
   */
  void resetPassword(String userName);

  /**
   * Deletes a user authentication record permanently.
   *
   * @param userName username
   */
  void deleteAuthentication(String userName);
}
