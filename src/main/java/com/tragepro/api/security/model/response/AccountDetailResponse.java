package io.tragepro.api.security.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDetailResponse {
    private String name;
    private String email;
    private String identifier;
    private Long phoneNumber;
    private Boolean isActive;
}
