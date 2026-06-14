package com.tragepro.api.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataTimeType {
  INTRADAY("INTRADAY"),
  HISTORICAL("HISTORICAL");

  private final String value;
}
