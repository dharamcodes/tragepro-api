package com.tragepro.api.domain.journal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeStatus {
  OPEN("Open"),
  CLOSED("Closed"),
  CANCELLED("Cancelled");

  private final String value;
}
