package com.tragepro.api.ohlcvdata.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SymbolData(@NotNull String id, @NotNull String name) {}
