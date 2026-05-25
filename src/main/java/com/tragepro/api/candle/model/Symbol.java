package com.tragepro.api.candle.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
public record Symbol(@NotNull @Field("id") String id, @NotNull String name) {}
