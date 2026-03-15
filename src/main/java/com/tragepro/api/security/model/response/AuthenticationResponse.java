package io.tragepro.api.security.model.response;

import io.tragepro.api.security.constant.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String userName;
    private RoleType role;
    private Boolean isActive;
}
