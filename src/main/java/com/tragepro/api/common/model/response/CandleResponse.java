package com.tragepro.api.common.model.response;

import com.tragepro.api.common.constant.DataTimeType;
import com.tragepro.api.common.model.CandleData;
import com.tragepro.api.common.model.SymbolData;
import lombok.Builder;

@Builder
public record CandleResponse(
    String id, DataTimeType dataTimeType, SymbolData symbolData, CandleData candleData) {
  public CandleResponse add(DataTimeType dataTimeType) {
    return new CandleResponse(id, dataTimeType, symbolData, candleData);
  }
}
