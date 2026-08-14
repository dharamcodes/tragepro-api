package com.tragepro.api.trading.model;

import java.math.BigDecimal;
import java.time.Instant;

public record TradePositionResponse(
    String id,
    String symbol,
    BigDecimal quantity,
    BigDecimal entryPrice,
    String side,
    String status,
    Instant createdAt) {}
