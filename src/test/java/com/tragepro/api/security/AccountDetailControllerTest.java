package com.tragepro.api.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.security.helper.JwtTokenHelper;
import com.tragepro.api.security.model.request.AccountDetailRequest;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class AccountDetailControllerTest extends ApiTestSetup {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    private AccountDetailRequest accountDetailRequest;

    @BeforeEach
    void setUp() {
        accountDetailRequest = AccountDetailRequest.builder()
                .name("Test Account")
                .email("testacc@example.com")
                .identifier("testAccount")
                .phoneNumber(9555318046L)
                .isActive(true)
                .build();
    }

    @Test
    void testCreateAccount_Success() throws Exception {
        mockMvc.perform(post("/config/v1/account")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDetailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.identifier").value("testAccount"))
                .andExpect(jsonPath("$.phoneNumber").value(9555318046L));
    }

    @Test
    void testCreateAccount_Exception() throws Exception {
        mockMvc.perform(post("/config/v1/account")).andExpect(status().is4xxClientError());

        mockMvc.perform(post("/config/v1/account")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDetailRequest)))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/config/v1/account")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        mockMvc.perform(post("/config/v1/account")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                AccountDetailRequest.builder().build())))
                .andExpect(status().isBadRequest());

        var token = jwtTokenHelper.generateToken(UUID.randomUUID().toString(), Map.of());
        mockMvc.perform(post("/config/v1/account")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                AccountDetailRequest.builder().build())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAccount_Success() throws Exception {

        mockMvc.perform(get("/config/v1/account/{identifier}", accountDetailRequest.getIdentifier())
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.identifier").value("testAccount"))
                .andExpect(jsonPath("$.phoneNumber").value(9555318046L));
    }

    @Test
    void testGetAccount_Exception() throws Exception {
        mockMvc.perform(get("/config/v1/account/{identifier}", " ").header("Authorization", authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid parameter."))
                .andExpect(jsonPath("$.errorCode").value("E0001"));

        mockMvc.perform(get("/config/v1/account/{identifier}", "test").header("Authorization", authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Data not found."))
                .andExpect(jsonPath("$.errorCode").value("E0004"));
    }

    @Test
    void testUpdateAccount_Success() throws Exception {
        accountDetailRequest.setEmail("updateduser@example.com");
        mockMvc.perform(put("/config/v1/account/{identifier}", "testAccount")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDetailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.identifier").value("testAccount"))
                .andExpect(jsonPath("$.phoneNumber").value(9555318046L));
    }

    @Test
    void testUpdateAccount_Exception() throws Exception {
        mockMvc.perform(put("/config/v1/account/{identifier}", " ")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDetailRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeactivateAccount_Success() throws Exception {
        mockMvc.perform(delete("/config/v1/account/{identifier}", "testAccount").header("Authorization", authToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/config/v1/account/{identifier}", "testAccount").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.identifier").value("testAccount"))
                .andExpect(jsonPath("$.phoneNumber").value(9555318046L))
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    void testDeactivateAccount_Exception() throws Exception {
        mockMvc.perform(delete("/config/v1/account/{identifier}", " ").header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }
}
