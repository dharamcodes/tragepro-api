package com.tragepro.api.domain.datafeed.request;

import lombok.Builder;

@Builder
public record WebSocketRequest(String id, String action) {}
