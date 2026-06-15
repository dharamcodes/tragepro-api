package com.tragepro.api.common.exception;

import com.tragepro.api.common.exception.constant.ErrorType;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

  private final ErrorType errorType;

  public AppException(ErrorType errorType) {
    super(errorType.getMessage());
    this.errorType = errorType;
  }
}
