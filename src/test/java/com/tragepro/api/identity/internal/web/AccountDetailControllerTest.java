package com.tragepro.api.identity.internal.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tragepro.api.common.ApiTestSetup;
import com.tragepro.api.identity.dto.AccountDetailRequest;
import com.tragepro.api.identity.internal.repository.AccountDetailRepository;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class AccountDetailControllerTest extends ApiTestSetup {

  @Autowired private AccountDetailRepository accountDetailRepository;

  private AccountDetailRequest accountDetailRequest;

  @BeforeEach
  void setUp() {
    // Wipe the account collection before every test so reused-container data
    // from a previous test or run cannot cause IncorrectResultSizeDataAccessException
    accountDetailRepository.deleteAll();

    accountDetailRequest =
        AccountDetailRequest.builder()
            .name("Test Account")
            .email("testacc@example.com")
            .identifier("testAccount")
            .phoneNumber(9555318046L)
            .isActive(true)
            .build();
  }

  // ─── helpers ────────────────────────────────────────────────────────────────

  /** POST /api/v1/account and assert 200. Used as a setup step in multi-step tests. */
  private void createAccount() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/account")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDetailRequest)))
        .andExpect(status().isOk());
  }

  // ─── create ─────────────────────────────────────────────────────────────────

  @Test
  void testCreateAccount_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/account")
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
    // No auth header → 4xx
    mockMvc.perform(post("/api/v1/account")).andExpect(status().is4xxClientError());

    // First creation succeeds (need account present for conflict check)
    createAccount();

    // Duplicate email → 409 Conflict
    mockMvc
        .perform(
            post("/api/v1/account")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDetailRequest)))
        .andExpect(status().isConflict());

    // Missing body → 400
    mockMvc
        .perform(
            post("/api/v1/account")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    // Empty / invalid body → 400
    mockMvc
        .perform(
            post("/api/v1/account")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDetailRequest.builder().build())))
        .andExpect(status().isBadRequest());

    // Token without a matching user → 4xx
    var token = jwtTokenHelper.generateToken(UUID.randomUUID().toString(), Map.of());
    mockMvc
        .perform(
            post("/api/v1/account")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDetailRequest.builder().build())))
        .andExpect(status().is4xxClientError());
  }

  // ─── get ────────────────────────────────────────────────────────────────────

  @Test
  void testGetAccount_Success() throws Exception {
    createAccount();

    mockMvc
        .perform(
            get("/api/v1/account/{identifier}", accountDetailRequest.identifier())
                .header("Authorization", authToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Account"))
        .andExpect(jsonPath("$.identifier").value("testAccount"))
        .andExpect(jsonPath("$.phoneNumber").value(9555318046L));
  }

  @Test
  void testGetAccount_Exception() throws Exception {
    mockMvc
        .perform(get("/api/v1/account/{identifier}", " ").header("Authorization", authToken))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid parameter."))
        .andExpect(jsonPath("$.errorCode").value("E0001"));

    mockMvc
        .perform(get("/api/v1/account/{identifier}", "test").header("Authorization", authToken))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Data not found."))
        .andExpect(jsonPath("$.errorCode").value("E0004"));
  }

  // ─── update ─────────────────────────────────────────────────────────────────

  @Test
  void testUpdateAccount_Success() throws Exception {
    createAccount();

    accountDetailRequest =
        AccountDetailRequest.builder()
            .name(accountDetailRequest.name())
            .email("updateduser@example.com")
            .identifier(accountDetailRequest.identifier())
            .phoneNumber(accountDetailRequest.phoneNumber())
            .isActive(accountDetailRequest.isActive())
            .build();

    mockMvc
        .perform(
            put("/api/v1/account/{identifier}", "testAccount")
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
    mockMvc
        .perform(
            put("/api/v1/account/{identifier}", " ")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDetailRequest)))
        .andExpect(status().isNotFound());
  }

  // ─── deactivate ─────────────────────────────────────────────────────────────

  @Test
  void testDeactivateAccount_Success() throws Exception {
    createAccount();

    mockMvc
        .perform(
            delete("/api/v1/account/{identifier}", "testAccount")
                .header("Authorization", authToken))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            get("/api/v1/account/{identifier}", "testAccount").header("Authorization", authToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Account"))
        .andExpect(jsonPath("$.identifier").value("testAccount"))
        .andExpect(jsonPath("$.phoneNumber").value(9555318046L))
        .andExpect(jsonPath("$.isActive").value(false));
  }

  @Test
  void testDeactivateAccount_Exception() throws Exception {
    mockMvc
        .perform(delete("/api/v1/account/{identifier}", " ").header("Authorization", authToken))
        .andExpect(status().isNotFound());
  }
}
