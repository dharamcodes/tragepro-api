package com.tragepro.api.domain.identity.response;

import lombok.Builder;

@Builder
public record LoginResponse(String userName, String token) {}
