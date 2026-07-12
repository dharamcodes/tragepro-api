package com.tragepro.api.identity.model.response;

import lombok.Builder;

@Builder
public record LoginResponse(String userName, String token) {}
