package com.tragepro.api.identity.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.domain.identity.constant.RoleType;
import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import com.tragepro.api.domain.identity.request.LoginRequest;
import com.tragepro.api.domain.identity.request.ResetPasswordRequest;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuthenticationControllerTest extends ApiTestSetup {

    private static final String PASSWORD_RESET_CLAIM = "PASSWORD_RESET_CLAIM";

    private AuthenticationRequest authenticationRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        authenticationRequest = AuthenticationRequest.builder()
                .userName(uuid)
                .email(uuid + "@example.com")
                .password("TestPassword123")
                .role(RoleType.APP_USER)
                .build();
        loginRequest = LoginRequest.builder()
                .userName(uuid)
                .password("TestPassword123")
                .build();
    }

    @Test
    void testSignup_Success() throws Exception {
        AuthenticationRequest newSignup = AuthenticationRequest.builder()
                .userName(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString() + "@example.com")
                .password("TestPassword123")
                .role(RoleType.APP_USER)
                .isActive(true)
                .build();
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSignup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(newSignup.userName()))
                .andExpect(jsonPath("$.isActive").value(newSignup.isActive()));
    }

    @Test
    void testSignup_Exception() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetByUserName_Success() throws Exception {
        mockMvc.perform(get("/api/v1/auth/find/{userName}", uuid).header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(uuid));
    }

    @Test
    void testGetByUserName_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/auth/find/{userName}", "nonexistentUser").header("Authorization", authToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateAuthenticationDetails_Success() throws Exception {
        AuthenticationRequest updatedRequest = AuthenticationRequest.builder()
                .userName(uuid)
                .email(uuid + "@example.com")
                .password("newPassword123")
                .role(RoleType.APP_USER)
                .isActive(true)
                .build();

        mockMvc.perform(put("/api/v1/auth/update/{userName}", uuid)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(uuid))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void testUpdateAuthenticationDetails_Exception() throws Exception {
        AuthenticationRequest updatedRequest = AuthenticationRequest.builder()
                .userName("nonexistentUser")
                .email("nonexistent@example.com")
                .password("irrelevant")
                .role(RoleType.APP_USER)
                .isActive(false)
                .build();

        mockMvc.perform(put("/api/v1/auth/update/{userName}", "nonexistentUser")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testLogin_Success() throws Exception {
        var loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
        loginResponse.andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLogin_Exception() throws Exception {
        LoginRequest nonexistentRequest = LoginRequest.builder()
                .userName("nonexistentUser")
                .password("somePassword123")
                .build();
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonexistentRequest)))
                .andExpect(status().isForbidden());

        LoginRequest wrongPasswordRequest =
                LoginRequest.builder().userName(uuid).password("wrongpass").build();
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongPasswordRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testResetPassword_Success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/reset-password/{userName}", loginRequest.userName())
                        .header("Authorization", authToken))
                .andExpect(status().isAccepted());
    }

    @Test
    void testResetPassword_Exception() throws Exception {
        mockMvc.perform(post(
                                "/api/v1/auth/reset-password/{userName}",
                                UUID.randomUUID().toString())
                        .header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testChangePassword_Success() throws Exception {
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.builder()
                .userName(uuid)
                .currentPassword("TestPassword123")
                .password("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();

        mockMvc.perform(put("/api/v1/auth/password")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testChangePassword_Exception() throws Exception {
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.builder()
                .userName(uuid)
                .currentPassword("TestPassword123")
                .password("NewPassword123!")
                .confirmPassword("NewPassword123")
                .build();

        mockMvc.perform(put("/api/v1/auth/password")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isBadRequest());

        ResetPasswordRequest resetPasswordRequestOtherUser = ResetPasswordRequest.builder()
                .userName(UUID.randomUUID().toString())
                .password("NewPassword123!")
                .confirmPassword("NewPassword123!")
                .build();
        mockMvc.perform(put("/api/v1/auth/password")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequestOtherUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeactivateUser_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/auth/deactivate/{userName}", authenticationRequest.userName())
                        .header("Authorization", authToken))
                .andExpect(status().isAccepted());
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeactivateUser_Exception() throws Exception {
        mockMvc.perform(delete("/api/v1/auth/deactivate/{userName}", "otherUser")
                        .header("Authorization", authToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/auth/delete/{userName}", authenticationRequest.userName())
                        .header("Authorization", authToken))
                .andExpect(status().isAccepted());
    }

    @Test
    void testDeleteUser_Exception() throws Exception {
        mockMvc.perform(delete("/api/v1/auth/delete/{userName}", "nonexistentUser")
                        .header("Authorization", authToken))
                .andExpect(status().isForbidden());
    }
}
