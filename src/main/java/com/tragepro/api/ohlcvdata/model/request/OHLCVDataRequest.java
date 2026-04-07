package com.tragepro.api.ohlcvdata.model.request;

import com.tragepro.api.ohlcvdata.model.OHLCVData;
import com.tragepro.api.ohlcvdata.model.SymbolData;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OHLCVDataRequest {
    @NotNull
    private SymbolData symbolData;

    @NotNull
    private OHLCVData ohlcvData;
}
