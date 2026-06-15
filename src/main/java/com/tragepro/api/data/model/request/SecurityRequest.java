package com.tragepro.api.data.model.request;

import lombok.Builder;

@Builder
public record SecurityRequest(
    String id,
    String exchange,
    String segment,
    Integer securityId,
    String isin,
    String instrument,
    String symbol,
    String symbolName,
    String name) {}
