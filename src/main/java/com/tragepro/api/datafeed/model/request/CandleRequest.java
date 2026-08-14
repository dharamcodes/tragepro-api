package com.tragepro.api.datafeed.model.request;

import com.tragepro.api.datafeed.constant.DataTimeType;
import com.tragepro.api.datafeed.model.CandleDataModel;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CandleRequest(
    DataTimeType dataTimeType,
    @NotNull SymbolDataModel symbolData,
    @NotNull CandleDataModel candleData) {
  public CandleRequest(SymbolDataModel symbolData, CandleDataModel candleData) {
    this(null, symbolData, candleData); // default without dataTimeType
  }

  public CandleRequest add(DataTimeType type) {
    return new CandleRequest(type, symbolData, candleData);
  }

  public CandleRequest setSymbolData(SymbolDataModel symbolData) {
    return new CandleRequest(this.dataTimeType, symbolData, this.candleData);
  }

  public CandleRequest withSymbolData(SymbolDataModel symbolData) {
    return new CandleRequest(this.dataTimeType, symbolData, this.candleData);
  }
}
