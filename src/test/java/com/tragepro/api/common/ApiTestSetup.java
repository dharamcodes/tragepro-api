package com.tragepro.api.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tragepro.api.identity.constant.RoleType;
import com.tragepro.api.identity.helper.JwtTokenHelper;
import com.tragepro.api.identity.model.request.AuthenticationRequest;
import com.tragepro.api.identity.model.request.LoginRequest;
import com.tragepro.api.identity.model.response.LoginResponse;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class ApiTestSetup extends ContainerConfig {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected JwtTokenHelper jwtTokenHelper;

  protected ObjectMapper objectMapper;

  protected String authToken;
  protected String uuid;

  @BeforeEach
  void setupUserAndAuth() throws Exception {
    objectMapper =
        JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
    uuid = UUID.randomUUID().toString();
    AuthenticationRequest signupRequest =
        AuthenticationRequest.builder()
            .userName(uuid)
            .email(uuid + "@example.com")
            .password("TestPassword123")
            .role(RoleType.APP_USER)
            .isActive(true)
            .build();

    mockMvc
        .perform(
            post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk());

    LoginRequest loginRequest =
        LoginRequest.builder().userName(uuid).password("TestPassword123").build();

    String loginResponse =
        mockMvc
            .perform(
                post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
    this.authToken = "Bearer " + response.token();
  }
}
