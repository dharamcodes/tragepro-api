package com.tragepro.api.identity.dto;

import lombok.Builder;

@Builder
public record LoginResponse(String userName, String token) {}
