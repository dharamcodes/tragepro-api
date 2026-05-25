package com.tragepro.api.candle.model.response;

import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleResponse {
    private Symbol symbol;
    private Candle candle;
}
