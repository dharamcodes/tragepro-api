package com.tragepro.api.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DatafeedState {
  INITIALIZED("INITIALIZED", 0),
  PROCESSING("PROCESSING", 1),
  COMPLETED("COMPLETED", 3);

  private final String name;
  private final int priority;
}
