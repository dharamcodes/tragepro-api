package com.tragepro.api.data.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FeedClientRequest(
    Integer securityId,
    String exchangeSegment,
    String instrument,
    String interval,
    Integer expiryCode,
    boolean oi,
    String fromDate,
    String toDate) {}
