package com.tragepro.api.identity.adapter.impl;

import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import com.tragepro.api.domain.identity.request.LoginRequest;
import com.tragepro.api.domain.identity.request.ResetPasswordRequest;
import com.tragepro.api.domain.identity.response.AuthenticationResponse;
import com.tragepro.api.domain.identity.response.LoginResponse;
import com.tragepro.api.identity.adapter.AuthenticationAdapter;
import com.tragepro.api.identity.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationAdapterImpl implements AuthenticationAdapter {
    private final AuthenticationService authenticationService;

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
    public void changePassword(ResetPasswordRequest resetPasswordRequest) {
        authenticationService.changePassword(resetPasswordRequest);
    }

    @Override
    public void deactivateAuthentication(String userName) {
        authenticationService.deactivateAuthentication(userName);
    }

    @Override
    public void resetPassword(String userName) {
        authenticationService.resetPassword(userName);
    }

    @Override
    public void deleteAuthentication(String userName) {
        authenticationService.deleteAuthentication(userName);
    }
}
