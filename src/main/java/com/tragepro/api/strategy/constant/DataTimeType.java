package com.tragepro.api.strategy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataTimeType {
  INTRADAY("INTRADAY"),
  HISTORICAL("HISTORICAL");

  private final String value;
}
