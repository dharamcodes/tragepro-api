package com.tragepro.api.domain.datafeed.response;

import lombok.Builder;

@Builder
public record LoadCandleResponse(String watchList, String message) {}
