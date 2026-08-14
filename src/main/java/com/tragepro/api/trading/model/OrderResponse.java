package com.tragepro.api.trading.model;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
    String id,
    String symbol,
    BigDecimal quantity,
    BigDecimal price,
    String orderType,
    String side,
    String status,
    Instant createdAt) {}
