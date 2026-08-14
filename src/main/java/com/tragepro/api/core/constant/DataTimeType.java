package com.tragepro.api.core.constant;

public enum DataTimeType {
  HISTORICAL("historical"),
  INTRADAY("intraday");

  private final String value;

  DataTimeType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
