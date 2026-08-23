package com.tragepro.api.identity.adapter.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.domain.identity.constant.RoleType;
import com.tragepro.api.domain.identity.request.AccountDetailRequest;
import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import com.tragepro.api.domain.identity.request.LoginRequest;
import com.tragepro.api.domain.identity.request.ResetPasswordRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;
import com.tragepro.api.domain.identity.response.AuthenticationResponse;
import com.tragepro.api.domain.identity.response.LoginResponse;
import com.tragepro.api.identity.service.AccountDetailService;
import com.tragepro.api.identity.service.AuthenticationService;
import com.tragepro.api.identity.service.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class IdentityAdapterTest {

  @Mock private AuthenticationService authenticationService;
  @Mock private AccountDetailService accountDetailService;
  @Mock private UserDetailService userDetailService;

  private AuthenticationAdapterImpl authenticationAdapter;
  private AccountDetailAdapterImpl accountDetailAdapter;
  private UserDetailAdapterImpl userDetailAdapter;

  @BeforeEach
  void setUp() {
    authenticationAdapter = new AuthenticationAdapterImpl(authenticationService);
    accountDetailAdapter = new AccountDetailAdapterImpl(accountDetailService);
    userDetailAdapter = new UserDetailAdapterImpl(userDetailService);
  }

  @Test
  void testAuthenticationAdapterMethods() {
    LoginRequest loginRequest = LoginRequest.builder().userName("trader").password("pass").build();
    LoginResponse loginResponse =
        LoginResponse.builder().userName("trader").token("jwt-token").build();

    when(authenticationService.login(loginRequest)).thenReturn(loginResponse);
    LoginResponse response = authenticationAdapter.loginUser(loginRequest);
    assertNotNull(response);
    assertEquals("trader", response.userName());

    AuthenticationRequest authRequest =
        AuthenticationRequest.builder().userName("trader").email("t@t.com").build();
    AuthenticationResponse authResponse =
        AuthenticationResponse.builder()
            .userName("trader")
            .role(RoleType.APP_USER)
            .isActive(true)
            .build();

    when(authenticationService.signup(authRequest)).thenReturn(authResponse);
    assertEquals(authResponse, authenticationAdapter.signup(authRequest));

    when(authenticationService.getByUserName("trader")).thenReturn(authResponse);
    assertEquals(authResponse, authenticationAdapter.getByUserName("trader"));

    when(authenticationService.updateAuthenticationDetails("trader", authRequest))
        .thenReturn(authResponse);
    assertEquals(
        authResponse, authenticationAdapter.updateAuthenticationDetails("trader", authRequest));

    ResetPasswordRequest resetReq =
        ResetPasswordRequest.builder().userName("trader").password("newP").build();
    authenticationAdapter.changePassword(resetReq);
    verify(authenticationService).changePassword(resetReq);

    authenticationAdapter.deactivateAuthentication("trader");
    verify(authenticationService).deactivateAuthentication("trader");

    authenticationAdapter.resetPassword("trader");
    verify(authenticationService).resetPassword("trader");

    authenticationAdapter.deleteAuthentication("trader");
    verify(authenticationService).deleteAuthentication("trader");
  }

  @Test
  void testAccountDetailAdapterMethods() {
    AccountDetailRequest request = AccountDetailRequest.builder().identifier("acc-123").build();
    AccountDetailResponse expectedAccount =
        AccountDetailResponse.builder().identifier("acc-123").build();

    when(accountDetailService.createAccount(request)).thenReturn(expectedAccount);
    assertEquals(expectedAccount, accountDetailAdapter.createAccount(request));

    when(accountDetailService.getAccount("acc-123")).thenReturn(expectedAccount);
    AccountDetailResponse response = accountDetailAdapter.getAccount("acc-123");
    assertNotNull(response);
    assertEquals("acc-123", response.identifier());

    when(accountDetailService.updateAccountDetails("acc-123", request)).thenReturn(expectedAccount);
    assertEquals(expectedAccount, accountDetailAdapter.updateAccountDetails("acc-123", request));

    accountDetailAdapter.deactivateAccount("acc-123");
    verify(accountDetailService).deactivateAccount("acc-123");
  }

  @Test
  void testUserDetailAdapterMethods() {
    UserDetails userDetails =
        User.builder().username("trader").password("pass").roles("USER").build();
    when(userDetailService.loadUserByUsername("trader")).thenReturn(userDetails);

    assertEquals(userDetails, userDetailAdapter.loadUserByUsername("trader"));
    verify(userDetailService).loadUserByUsername("trader");
  }
}
