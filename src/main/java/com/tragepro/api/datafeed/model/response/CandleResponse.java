package com.tragepro.api.datafeed.model.response;

import com.tragepro.api.datafeed.constant.DataTimeType;
import com.tragepro.api.datafeed.model.CandleDataModel;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import lombok.Builder;

@Builder
public record CandleResponse(
    String id, DataTimeType dataTimeType, SymbolDataModel symbolData, CandleDataModel candleData) {
  public CandleResponse add(DataTimeType dataTimeType) {
    return new CandleResponse(id, dataTimeType, symbolData, candleData);
  }
}
