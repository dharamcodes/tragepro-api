package com.tragepro.api.domain.datafeed;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SymbolDataModel(
        @NotNull String symbol, @NotNull String name) {}
