package com.tragepro.api.identity.auth.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class LoginResponse {
    private String userName;
    private String token;
}
