package com.tragepro.api.data.model.response;

import lombok.Builder;

@Builder
public record LoadCandleResponse(String watchList, String message) {}
