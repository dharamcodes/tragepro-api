package com.tragepro.api.identity.account.model.response;

import lombok.Builder;

@Builder
public record AccountDetailResponse(
    String name, String email, String identifier, Long phoneNumber, Boolean isActive) {}
