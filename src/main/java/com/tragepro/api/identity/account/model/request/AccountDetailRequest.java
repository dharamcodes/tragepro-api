package com.tragepro.api.identity.account.model.request;

import com.tragepro.api.common.identifier.annotation.Base32IdGen;
import com.tragepro.api.common.identifier.annotation.Identifier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Base32IdGen
public record AccountDetailRequest(
        @NotBlank(message = "User name cannot be blank") String name,

        @Email(message = "Invalid email format") @NotBlank(message = "Email cannot be blank")
        String email,

        @Identifier String identifier,

        @NotNull(message = "Phone number cannot be null")
        @Min(value = 1000000000L, message = "Phone number must be at least 10 digits")
        @Max(value = 999999999999L, message = "Phone number must be at most 12 digits")
        Long phoneNumber,

        Boolean isActive) {}
