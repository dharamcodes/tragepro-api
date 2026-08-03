package com.tragepro.api.identity.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String userName, RoleType role, Boolean isActive) {}
