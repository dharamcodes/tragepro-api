package com.tragepro.api.domain.trading.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record OrderRequest(
        @NotBlank(message = "Symbol is required") String symbol,

        @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be positive")
        BigDecimal quantity,

        @NotNull(message = "Price is required") @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotBlank(message = "Order type is required") String orderType,
        @NotBlank(message = "Side is required") String side) {}
