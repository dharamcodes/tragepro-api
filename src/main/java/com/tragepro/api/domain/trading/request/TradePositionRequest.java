package com.tragepro.api.domain.trading.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TradePositionRequest(
        @NotBlank(message = "Symbol is required") String symbol,

        @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be positive")
        BigDecimal quantity,

        @NotNull(message = "Entry price is required") @Positive(message = "Entry price must be positive")
        BigDecimal entryPrice,

        @NotBlank(message = "Side is required") String side) {}
