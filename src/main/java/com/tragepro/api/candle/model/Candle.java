package com.tragepro.api.candle.model;

import lombok.Builder;

@Builder
public record Candle(long timestamp, double open, double high, double low, double close, double volume) {}
