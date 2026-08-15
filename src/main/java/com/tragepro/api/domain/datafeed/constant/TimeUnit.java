package com.tragepro.api.domain.datafeed.constant;

import static java.util.Arrays.stream;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeUnit {
  MINUTE,
  HOUR,
  DAY,
  WEEK,
  MONTH;

  public static TimeUnit of(String value) {
    if (value == null) return null;
    String upper = value.trim().toUpperCase();
    return stream(values())
        .filter(t -> t.name().equalsIgnoreCase(upper))
        .findFirst()
        .orElseThrow(() -> new AppException(ErrorType.INTERNAL_ERROR));
  }
}
