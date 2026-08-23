package com.tragepro.api.domain.identity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        @NotBlank(message = "Username must not be blank") String userName,
        String currentPassword,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "Confirm password must not be blank")
        String confirmPassword) {

    public ResetPasswordRequest(String userName, String password, String confirmPassword) {
        this(userName, null, password, confirmPassword);
    }
}
