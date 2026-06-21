package com.tragepro.api.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SymbolData(@NotNull String symbol, @NotNull String name) {}
