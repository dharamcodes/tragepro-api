package io.tragepro.api.application.service;

import io.tragepro.api.security.model.request.AuthenticationRequest;
import io.tragepro.api.security.model.request.LoginRequest;
import io.tragepro.api.security.model.request.ResetPasswordRequest;
import io.tragepro.api.security.model.response.AuthenticationResponse;
import io.tragepro.api.security.model.response.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest);

    AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

    AuthenticationResponse getByUserName(String userName);

    AuthenticationResponse updateAuthenticationDetails(String userName, AuthenticationRequest authenticationRequest);

    void changePassword(ResetPasswordRequest resetPasswordRequest);

    void deactivateAuthentication(String userName);

    void resetPassword(String userName);
}
