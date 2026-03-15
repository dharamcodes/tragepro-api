package io.tragepro.api.security.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String userName;
    private String token;
}
