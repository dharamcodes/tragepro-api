package com.tragepro.api.security.model.response;

import com.tragepro.api.security.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private String userName;
    private RoleType role;
    private Boolean isActive;
}
