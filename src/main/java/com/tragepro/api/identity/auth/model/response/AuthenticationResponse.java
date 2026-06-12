package com.tragepro.api.identity.auth.model.response;

import com.tragepro.api.identity.auth.constant.RoleType;
import lombok.Builder;

@Builder
public record AuthenticationResponse(String userName, RoleType role, Boolean isActive) {}
