package com.tragepro.api.marketdata.candle.model.response;

import com.tragepro.api.marketdata.candle.model.CandleData;
import com.tragepro.api.marketdata.candle.model.SymbolData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleResponse {
    private String id;
    private SymbolData symbolData;
    private CandleData candleData;
}
