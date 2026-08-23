package com.tragepro.api.domain.identity.response;

import lombok.Builder;

@Builder
public record AccountDetailResponse(String name, String email, String identifier, Long phoneNumber, Boolean isActive) {}
