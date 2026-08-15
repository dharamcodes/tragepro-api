package com.tragepro.api.domain.datafeed.response;

import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.DataTimeType;
import lombok.Builder;

@Builder
public record CandleResponse(
    String id, DataTimeType dataTimeType, SymbolDataModel symbolData, CandleDataModel candleData) {
  public CandleResponse add(DataTimeType dataTimeType) {
    return new CandleResponse(id, dataTimeType, symbolData, candleData);
  }
}
