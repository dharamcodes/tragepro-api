package io.tragepro.api.exception.impl;

import io.tragepro.api.exception.constant.ErrorType;
import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {

    private final ErrorType errorType;

    public ServerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
