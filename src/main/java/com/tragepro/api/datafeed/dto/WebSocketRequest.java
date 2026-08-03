package com.tragepro.api.datafeed.dto;

import lombok.Builder;

@Builder
public record WebSocketRequest(String id, String action) {}
