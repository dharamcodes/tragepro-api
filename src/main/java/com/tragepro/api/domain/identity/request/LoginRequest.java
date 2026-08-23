package com.tragepro.api.domain.identity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank(message = "User name cannot be blank") String userName,
        @NotBlank(message = "Password cannot be blank") String password) {}
