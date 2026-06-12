package com.tragepro.api.data.model.request;

import lombok.Builder;

@Builder
public record WebSocketRequest(String id, String action) {
    public WebSocketRequest() {
        this(null, null);
    }
}
