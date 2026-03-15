package com.tragepro.api.exception;

import com.tragepro.api.exception.constant.ErrorType;
import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {

    private final ErrorType errorType;

    public ServerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
