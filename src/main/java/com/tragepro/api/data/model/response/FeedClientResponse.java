package com.tragepro.api.data.model.response;

import java.util.List;

public record FeedClientResponse(
    List<Double> open,
    List<Double> high,
    List<Double> low,
    List<Double> close,
    List<Long> volume,
    List<Long> timestamp,
    List<Long> openInterest) {}
