package com.tragepro.api.datafeed.constant;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InstrumentType {
  INDEX("INDEX", "Index"),
  FUTIDX("FUTIDX", "Futures of Index"),
  OPTIDX("OPTIDX", "Options of Index"),
  EQUITY("EQUITY", "Equity"),
  FUTSTK("FUTSTK", "Futures of Stock"),
  OPTSTK("OPTSTK", "Options of Stock"),
  FUTCOM("FUTCOM", "Futures of Commodity"),
  OPTFUT("OPTFUT", "Options of Commodity Futures"),
  FUTCUR("FUTCUR", "Futures of Currency"),
  OPTCUR("OPTCUR", "Options of Currency");

  private final String value;
  private final String detail;

  public static InstrumentType of(String instrument) {
    return Arrays.stream(values())
        .filter(seg -> seg.getValue().equalsIgnoreCase(instrument))
        .findFirst()
        .orElseThrow(() -> new AppException(ErrorType.INTERNAL_ERROR));
  }
}
