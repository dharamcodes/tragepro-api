package com.tragepro.api.domain.identity.request;

import com.tragepro.api.domain.identity.constant.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
        @Email(message = "Invalid email format") @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "User name cannot be blank") String userName,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotNull(message = "Role cannot be null") RoleType role,
        Boolean isActive) {}
