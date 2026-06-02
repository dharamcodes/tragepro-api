package com.tragepro.api.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SymbolData(@NotNull String id, @NotNull String name) {}
