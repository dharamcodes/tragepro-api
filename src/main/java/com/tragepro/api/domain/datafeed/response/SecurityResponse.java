package com.tragepro.api.domain.datafeed.response;

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
