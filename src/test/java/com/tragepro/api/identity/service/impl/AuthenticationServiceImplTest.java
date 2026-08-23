package com.tragepro.api.identity.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.config.JwtTokenHelper;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.domain.identity.constant.RoleType;
import com.tragepro.api.domain.identity.entity.AuthenticationEntity;
import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import com.tragepro.api.domain.identity.request.LoginRequest;
import com.tragepro.api.domain.identity.request.ResetPasswordRequest;
import com.tragepro.api.domain.identity.response.AuthenticationResponse;
import com.tragepro.api.domain.identity.response.LoginResponse;
import com.tragepro.api.identity.core.repository.AuthenticationRepository;
import com.tragepro.api.identity.service.mapper.AuthenticationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private MapperFactory mapperFactory;

    @Mock
    private AuthenticationMapper authenticationMapper;

    @Mock
    private JwtTokenHelper jwtTokenHelper;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private AuthenticationEntity user;

    @BeforeEach
    void setUp() {
        user = new AuthenticationEntity(
                "id1", "user1@test.com", "user1", "hashedPass", RoleType.APP_USER, true, java.util.Set.of());
    }

    @Test
    void testLogin_Success() {
        LoginRequest req = new LoginRequest("user1", "pass1");
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(user);
        when(jwtTokenHelper.generateToken(anyString(), anyMap())).thenReturn("token123");

        LoginResponse response = authenticationService.login(req);
        assertEquals("user1", response.userName());
        assertEquals("token123", response.token());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest req = new LoginRequest("user1", "pass1");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        assertThrows(AppException.class, () -> authenticationService.login(req));
    }

    @Test
    void testLogin_InvalidPassword() {
        LoginRequest req = new LoginRequest("user1", "pass1");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        assertThrows(AppException.class, () -> authenticationService.login(req));
    }

    @Test
    void testSignup_Success() {
        AuthenticationRequest req = AuthenticationRequest.builder()
                .userName("user1")
                .password("pass1")
                .email("user1@test.com")
                .role(RoleType.SUPER_USER)
                .build();

        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserName("user1")).thenReturn(null);
        when(bCryptPasswordEncoder.encode("pass1")).thenReturn("encoded");
        when(authenticationMapper.requestToEntity(req)).thenReturn(user);
        when(authenticationRepository.save(user)).thenReturn(user);
        when(authenticationMapper.entityToResponse(user))
                .thenReturn(AuthenticationResponse.builder().userName("user1").build());

        AuthenticationResponse response = authenticationService.signup(req);
        assertEquals("user1", response.userName());
        assertEquals(RoleType.APP_USER, user.getRole());
    }

    @Test
    void testSignup_UserExists() {
        AuthenticationRequest req = AuthenticationRequest.builder()
                .userName("user1")
                .password("pass1")
                .build();

        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserName("user1")).thenReturn(user);

        assertThrows(AppException.class, () -> authenticationService.signup(req));
    }

    @Test
    void testSignup_UserNameBlank() {
        AuthenticationRequest req = AuthenticationRequest.builder()
                .userName("")
                .password("pass1")
                .email("user1@test.com")
                .build();

        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserName("")).thenReturn(null);
        when(bCryptPasswordEncoder.encode("pass1")).thenReturn("encoded");
        when(authenticationMapper.requestToEntity(req)).thenReturn(user);
        when(authenticationRepository.save(user)).thenReturn(user);
        when(authenticationMapper.entityToResponse(user))
                .thenReturn(AuthenticationResponse.builder()
                        .userName("user1@test.com")
                        .build());

        AuthenticationResponse response = authenticationService.signup(req);
        assertEquals("user1@test.com", response.userName());
    }

    @Test
    void testGetByUserName_Success() {
        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserName("user1")).thenReturn(user);
        when(authenticationMapper.entityToResponse(user))
                .thenReturn(AuthenticationResponse.builder().userName("user1").build());

        AuthenticationResponse response = authenticationService.getByUserName("user1");
        assertEquals("user1", response.userName());
    }

    @Test
    void testGetByUserName_NotFound() {
        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserName("user1")).thenReturn(null);

        assertThrows(AppException.class, () -> authenticationService.getByUserName("user1"));
    }

    @Test
    void testUpdateAuthenticationDetails_Success() {
        AuthenticationRequest req =
                AuthenticationRequest.builder().userName("user1").build();

        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserName("user1")).thenReturn(user);
        when(authenticationRepository.save(user)).thenReturn(user);
        when(authenticationMapper.entityToResponse(user))
                .thenReturn(AuthenticationResponse.builder().userName("user1").build());

        AuthenticationResponse response = authenticationService.updateAuthenticationDetails("user1", req);
        assertEquals("user1", response.userName());
        verify(authenticationMapper).merge(req, user);
    }

    @Test
    void testUpdateAuthenticationDetails_NotFound() {
        AuthenticationRequest req =
                AuthenticationRequest.builder().userName("user1").build();
        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserName("user1")).thenReturn(null);

        assertThrows(AppException.class, () -> authenticationService.updateAuthenticationDetails("user1", req));
    }

    @Test
    void testChangePassword_Success() {
        ResetPasswordRequest req = ResetPasswordRequest.builder()
                .userName("user1")
                .currentPassword("oldPass")
                .password("newPass")
                .confirmPassword("newPass")
                .build();
        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(user);
        when(bCryptPasswordEncoder.matches("oldPass", "hashedPass")).thenReturn(true);
        when(bCryptPasswordEncoder.encode("newPass")).thenReturn("encodedNew");

        assertDoesNotThrow(() -> authenticationService.changePassword(req));
        verify(authenticationRepository).save(user);
    }

    @Test
    void testChangePassword_InvalidCurrentPassword() {
        ResetPasswordRequest req = ResetPasswordRequest.builder()
                .userName("user1")
                .currentPassword("wrongPass")
                .password("newPass")
                .confirmPassword("newPass")
                .build();
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(user);
        when(bCryptPasswordEncoder.matches("wrongPass", "hashedPass")).thenReturn(false);

        assertThrows(AppException.class, () -> authenticationService.changePassword(req));
    }

    @Test
    void testChangePassword_Mismatch() {
        ResetPasswordRequest req = new ResetPasswordRequest("user1", "newPass", "otherPass");
        assertThrows(AppException.class, () -> authenticationService.changePassword(req));
    }

    @Test
    void testChangePassword_UserNotFound() {
        ResetPasswordRequest req = new ResetPasswordRequest("user1", "newPass", "newPass");
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(null);

        assertThrows(AppException.class, () -> authenticationService.changePassword(req));
    }

    @Test
    void testDeactivateAuthentication_Success() {
        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(user);

        assertDoesNotThrow(() -> authenticationService.deactivateAuthentication("user1"));
        verify(authenticationRepository).save(user);
    }

    @Test
    void testDeactivateAuthentication_NotFound() {
        when(mapperFactory.getMapper(AuthenticationMapper.class)).thenReturn(authenticationMapper);
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(null);

        assertThrows(AppException.class, () -> authenticationService.deactivateAuthentication("user1"));
    }

    @Test
    void testResetPassword_Success() {
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(user);
        when(jwtTokenHelper.generateResetPasswordToken(eq("user1"), anyMap())).thenReturn("resetToken");

        assertDoesNotThrow(() -> authenticationService.resetPassword("user1"));
    }

    @Test
    void testResetPassword_NotFound() {
        when(authenticationRepository.findByUserNameAndIsActive("user1", true)).thenReturn(null);

        assertThrows(AppException.class, () -> authenticationService.resetPassword("user1"));
    }

    @Test
    void testDeleteAuthentication_Success() {
        when(authenticationRepository.findByUserName("user1")).thenReturn(user);

        assertDoesNotThrow(() -> authenticationService.deleteAuthentication("user1"));
        verify(authenticationRepository).delete(user);
    }

    @Test
    void testDeleteAuthentication_NotFound() {
        when(authenticationRepository.findByUserName("user1")).thenReturn(null);

        assertThrows(AppException.class, () -> authenticationService.deleteAuthentication("user1"));
    }
}
