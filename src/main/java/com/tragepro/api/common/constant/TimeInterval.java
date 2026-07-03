package com.tragepro.api.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeInterval {
  MIN_1("1"),
  MIN_5("5"),
  MIN_15("15"),
  MIN_25("25"),
  MIN_60("60");

  private final String value;
}
