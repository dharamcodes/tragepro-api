package com.tragepro.api.identity.adapter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.domain.identity.request.LoginRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;
import com.tragepro.api.domain.identity.response.LoginResponse;
import com.tragepro.api.identity.service.AccountDetailService;
import com.tragepro.api.identity.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentityAdapterTest {

  @Mock private AuthenticationService authenticationService;
  @Mock private AccountDetailService accountDetailService;

  private AuthenticationAdapterImpl authenticationAdapter;
  private AccountDetailAdapterImpl accountDetailAdapter;

  @BeforeEach
  void setUp() {
    authenticationAdapter = new AuthenticationAdapterImpl(authenticationService);
    accountDetailAdapter = new AccountDetailAdapterImpl(accountDetailService);
  }

  @Test
  void testLogin() {
    LoginRequest loginRequest = LoginRequest.builder().userName("trader").password("pass").build();
    LoginResponse loginResponse =
        LoginResponse.builder().userName("trader").token("jwt-token").build();

    when(authenticationService.login(loginRequest)).thenReturn(loginResponse);

    LoginResponse response = authenticationAdapter.loginUser(loginRequest);

    assertNotNull(response);
    assertEquals("trader", response.userName());
    verify(authenticationService).login(loginRequest);
  }

  @Test
  void testGetAccount() {
    AccountDetailResponse expectedAccount =
        AccountDetailResponse.builder().identifier("acc-123").build();
    when(accountDetailService.getAccount("acc-123")).thenReturn(expectedAccount);

    AccountDetailResponse response = accountDetailAdapter.getAccount("acc-123");

    assertNotNull(response);
    assertEquals("acc-123", response.identifier());
    verify(accountDetailService).getAccount("acc-123");
  }
}
