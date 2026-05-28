package com.tragepro.api.identity.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountDetailResponse {
    private String name;
    private String email;
    private String identifier;
    private Long phoneNumber;
    private Boolean isActive;
}
