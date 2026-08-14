package com.tragepro.api.identity.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.identity.constant.RoleType;
import com.tragepro.api.identity.model.request.LoginRequest;
import com.tragepro.api.identity.model.response.AccountDetailResponse;
import com.tragepro.api.identity.model.response.AuthenticationResponse;
import com.tragepro.api.identity.model.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentityAdapterTest {

  @Mock private AuthenticationService authenticationService;

  @Mock private AccountDetailService accountDetailService;

  private IdentityAdapterImpl identityAdapter;

  @BeforeEach
  void setUp() {
    identityAdapter = new IdentityAdapterImpl(authenticationService, accountDetailService);
  }

  @Test
  void testLogin() {
    LoginRequest loginRequest = LoginRequest.builder().userName("trader").password("pass").build();
    LoginResponse loginResponse =
        LoginResponse.builder().userName("trader").token("jwt-token").build();
    AuthenticationResponse authResponse =
        AuthenticationResponse.builder()
            .userName("trader")
            .role(RoleType.APP_USER)
            .isActive(true)
            .build();

    when(authenticationService.login(loginRequest)).thenReturn(loginResponse);
    when(authenticationService.getByUserName("trader")).thenReturn(authResponse);

    AuthenticationResponse response = identityAdapter.login(loginRequest);

    assertNotNull(response);
    assertEquals("trader", response.userName());
    verify(authenticationService).login(loginRequest);
    verify(authenticationService).getByUserName("trader");
  }

  @Test
  void testGetAccountById() {
    AccountDetailResponse expectedAccount =
        AccountDetailResponse.builder().identifier("acc-123").build();
    when(accountDetailService.getAccount("acc-123")).thenReturn(expectedAccount);

    AccountDetailResponse response = identityAdapter.getAccountById("acc-123");

    assertNotNull(response);
    assertEquals("acc-123", response.identifier());
    verify(accountDetailService).getAccount("acc-123");
  }
}
