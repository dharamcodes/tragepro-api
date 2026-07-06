package com.tragepro.api.datafeed.model.request;

import lombok.Builder;

@Builder
public record WebSocketRequest(String id, String action) {}
