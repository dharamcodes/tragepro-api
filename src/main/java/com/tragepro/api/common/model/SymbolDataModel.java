package com.tragepro.api.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SymbolDataModel(@NotNull String symbol, @NotNull String name) {}
