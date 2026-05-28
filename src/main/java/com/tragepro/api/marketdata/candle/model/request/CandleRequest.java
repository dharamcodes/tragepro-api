package com.tragepro.api.marketdata.candle.model.request;

import com.tragepro.api.marketdata.candle.model.CandleData;
import com.tragepro.api.marketdata.candle.model.SymbolData;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleRequest {
    @NotNull
    private SymbolData symbolData;

    @NotNull
    private CandleData candleData;
}
