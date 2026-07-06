package com.tragepro.api.common.model;

import lombok.Builder;

@Builder
public record CandleDataModel(
    long timestamp, double open, double high, double low, double close, double volume) {}
