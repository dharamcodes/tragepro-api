package com.tragepro.api.ohlcvdata.model;

import lombok.Builder;

@Builder
public record OHLCVData(long timestamp, double open, double high, double low, double close, double volume) {}
