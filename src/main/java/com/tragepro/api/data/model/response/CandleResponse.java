package com.tragepro.api.data.model.response;

import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
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
