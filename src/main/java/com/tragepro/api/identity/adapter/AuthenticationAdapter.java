package com.tragepro.api.identity.adapter;

import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import com.tragepro.api.domain.identity.request.LoginRequest;
import com.tragepro.api.domain.identity.request.ResetPasswordRequest;
import com.tragepro.api.domain.identity.response.AuthenticationResponse;
import com.tragepro.api.domain.identity.response.LoginResponse;

public interface AuthenticationAdapter {
    LoginResponse loginUser(LoginRequest loginRequest);

    AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

    AuthenticationResponse getByUserName(String userName);

    AuthenticationResponse updateAuthenticationDetails(String userName, AuthenticationRequest authenticationRequest);

    void changePassword(ResetPasswordRequest resetPasswordRequest);

    void deactivateAuthentication(String userName);

    void resetPassword(String userName);

    void deleteAuthentication(String userName);
}
