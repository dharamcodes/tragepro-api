package com.tragepro.api.ohlcvdata.model.response;

import com.tragepro.api.ohlcvdata.model.OHLCVData;
import com.tragepro.api.ohlcvdata.model.SymbolData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OHLCVDataResponse {
    private SymbolData symbolData;
    private OHLCVData ohlcvData;
}
