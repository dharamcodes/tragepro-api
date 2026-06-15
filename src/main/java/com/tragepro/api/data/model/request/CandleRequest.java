package com.tragepro.api.data.model.request;

import com.tragepro.api.common.constant.DataTimeType;
import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CandleRequest(
    DataTimeType dataTimeType, @NotNull SymbolData symbolData, @NotNull CandleData candleData) {
  public CandleRequest(SymbolData symbolData, CandleData candleData) {
    this(null, symbolData, candleData); // default without dataTimeType
  }

  public CandleRequest add(DataTimeType type) {
    return new CandleRequest(type, symbolData, candleData);
  }
}
