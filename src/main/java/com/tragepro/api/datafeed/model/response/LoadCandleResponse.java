package com.tragepro.api.datafeed.model.response;

import lombok.Builder;

@Builder
public record LoadCandleResponse(String watchList, String message) {}
