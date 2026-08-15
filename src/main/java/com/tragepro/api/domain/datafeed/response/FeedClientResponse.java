package com.tragepro.api.domain.datafeed.response;

import java.util.List;
import lombok.Builder;

@Builder
public record FeedClientResponse(
    List<Double> open,
    List<Double> high,
    List<Double> low,
    List<Double> close,
    List<Long> volume,
    List<Long> timestamp,
    List<Long> openInterest) {}
