package com.tragepro.api.common.model.response;

import com.tragepro.api.common.constant.DataTimeType;
import com.tragepro.api.common.model.CandleDataModel;
import com.tragepro.api.common.model.SymbolDataModel;
import lombok.Builder;

@Builder
public record CandleResponse(
    String id, DataTimeType dataTimeType, SymbolDataModel symbolData, CandleDataModel candleData) {
  public CandleResponse add(DataTimeType dataTimeType) {
    return new CandleResponse(id, dataTimeType, symbolData, candleData);
  }
}
