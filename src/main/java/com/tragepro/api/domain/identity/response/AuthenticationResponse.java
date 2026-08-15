package com.tragepro.api.domain.identity.response;

import com.tragepro.api.domain.identity.constant.RoleType;
import lombok.Builder;

@Builder
public record AuthenticationResponse(String userName, RoleType role, Boolean isActive) {}
