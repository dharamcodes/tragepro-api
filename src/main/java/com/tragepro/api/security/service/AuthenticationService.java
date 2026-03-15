package com.tragepro.api.security.service;

import com.tragepro.api.security.model.request.AuthenticationRequest;
import com.tragepro.api.security.model.request.LoginRequest;
import com.tragepro.api.security.model.request.ResetPasswordRequest;
import com.tragepro.api.security.model.response.AuthenticationResponse;
import com.tragepro.api.security.model.response.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest);

    AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

    AuthenticationResponse getByUserName(String userName);

    AuthenticationResponse updateAuthenticationDetails(String userName, AuthenticationRequest authenticationRequest);

    void changePassword(ResetPasswordRequest resetPasswordRequest);

    void deactivateAuthentication(String userName);

    void resetPassword(String userName);
}
