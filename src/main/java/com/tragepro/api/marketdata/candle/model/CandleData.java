package com.tragepro.api.marketdata.candle.model;

import lombok.Builder;

@Builder
public record CandleData(long timestamp, double open, double high, double low, double close, double volume) {}
