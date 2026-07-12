package com.tragepro.api.identity.model.response;

import com.tragepro.api.identity.constant.RoleType;
import lombok.Builder;

@Builder
public record AuthenticationResponse(String userName, RoleType role, Boolean isActive) {}
