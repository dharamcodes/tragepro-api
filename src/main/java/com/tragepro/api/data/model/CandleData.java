package com.tragepro.api.data.model;

import lombok.Builder;

@Builder
public record CandleData(long timestamp, double open, double high, double low, double close, double volume) {}
