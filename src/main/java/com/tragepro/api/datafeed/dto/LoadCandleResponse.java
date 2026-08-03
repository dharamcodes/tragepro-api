package com.tragepro.api.datafeed.dto;

import lombok.Builder;

@Builder
public record LoadCandleResponse(String watchList, String message) {}
