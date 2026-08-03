package com.tragepro.api.journal.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeType {
  LONG("Long"),
  SHORT("Short");

  private final String value;
}
