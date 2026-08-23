package com.tragepro.api.common.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.domain.identity.constant.RoleType;
import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityCsrfTest extends ContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testPostWithoutCsrf_Forbidden() throws Exception {
        String uuid = UUID.randomUUID().toString();
        AuthenticationRequest signupRequest = AuthenticationRequest.builder()
                .userName(uuid)
                .email(uuid + "@example.com")
                .password("TestPassword123")
                .role(RoleType.APP_USER)
                .isActive(true)
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostWithCsrf_Success() throws Exception {
        String uuid = UUID.randomUUID().toString();
        AuthenticationRequest signupRequest = AuthenticationRequest.builder()
                .userName(uuid)
                .email(uuid + "@example.com")
                .password("TestPassword123")
                .role(RoleType.APP_USER)
                .isActive(true)
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testCorsPreflight_ReturnsCorsHeaders() throws Exception {
        mockMvc.perform(options("/api/v1/auth/signup")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().exists("Access-Control-Allow-Methods"));
    }
}
