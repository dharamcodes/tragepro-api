package com.tragepro.api.datafeed.dto;

import com.tragepro.api.common.constant.DataTimeType;
import com.tragepro.api.common.model.CandleDataModel;
import com.tragepro.api.common.model.SymbolDataModel;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record CandleRequest(
    DataTimeType dataTimeType,
    @NotNull SymbolDataModel symbolData,
    @NotNull CandleDataModel candleData) {

  public CandleRequest(SymbolDataModel symbolData, CandleDataModel candleData) {
    this(null, symbolData, candleData);
  }

  public static CandleRequest of(SymbolDataModel symbolData, CandleDataModel candleData) {
    return new CandleRequest(null, symbolData, candleData);
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
