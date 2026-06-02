package com.tragepro.api.data.model.request;

import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
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
