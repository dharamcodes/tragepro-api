package com.tragepro.api.data.model.request;

import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CandleRequest(
        @NotNull SymbolData symbolData, @NotNull CandleData candleData) {
    public CandleRequest() {
        this(null, null);
    }
}
