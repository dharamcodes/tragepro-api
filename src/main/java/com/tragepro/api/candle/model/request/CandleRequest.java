package com.tragepro.api.candle.model.request;

import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleRequest {
    @NotNull
    private Symbol symbol;

    @NotNull
    private Candle candle;
}
