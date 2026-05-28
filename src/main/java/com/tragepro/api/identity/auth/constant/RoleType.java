package com.tragepro.api.identity.auth.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    APP_USER("APP_USER"),
    APP_MANAGER("APP_MANAGER"),
    APP_ADMIN("APP_ADMIN"),
    SUPER_USER("SUPER_USER"),
    PASSWORD_RESET_CLAIM("PASSWORD_RESET_CLAIM");

    private final String value;
}
