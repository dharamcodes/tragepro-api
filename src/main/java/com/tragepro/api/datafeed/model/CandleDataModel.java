package com.tragepro.api.datafeed.model;

import lombok.Builder;

@Builder
public record CandleDataModel(
    long timestamp, double open, double high, double low, double close, long volume) {}
