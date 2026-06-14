package com.tragepro.api.data.model.response;

import lombok.Builder;

@Builder
public record SecurityResponse(
    String exchange,
    String segment,
    Integer securityId,
    String isin,
    String instrument,
    String symbol,
    String symbolName,
    String name) {}
