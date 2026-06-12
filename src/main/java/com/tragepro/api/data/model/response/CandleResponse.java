package com.tragepro.api.data.model.response;

import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
import lombok.Builder;

@Builder
public record CandleResponse(String id, SymbolData symbolData, CandleData candleData) {
    public CandleResponse() {
        this(null, null, null);
    }
}
