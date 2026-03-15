package com.tragepro.api.exception;

import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.exception.model.AppErrorDto;
import com.tragepro.api.exception.model.ServerErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerAdvise {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ServerErrorDto> handle(ServerException baseException, HttpServletRequest request) {
        ErrorType errorType = baseException.getErrorType();
        return ResponseEntity.status(errorType.getCode())
                .body(ServerErrorDto.builder()
                        .errorCode(errorType.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .message(messageSource.getMessage(errorType.getMessage(), null, Locale.ENGLISH))
                        .uri(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler({AppException.class, AccessDeniedException.class})
    public ResponseEntity<AppErrorDto> handle(AppException appException) {
        ErrorType errorType = appException.getErrorType();
        return ResponseEntity.status(appException.getErrorType().getCode())
                .body(AppErrorDto.builder()
                        .errorCode(errorType.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .message(messageSource.getMessage(errorType.getMessage(), null, Locale.ENGLISH))
                        .build());
    }

    @ExceptionHandler({ConstraintViolationException.class, RuntimeException.class})
    public ResponseEntity<AppErrorDto> handle(RuntimeException runtimeException) {
        ErrorType errorType = ErrorType.INTERNAL_ERROR;
        return ResponseEntity.status(errorType.getCode())
                .body(AppErrorDto.builder()
                        .errorCode(errorType.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .message(runtimeException.getLocalizedMessage())
                        .build());
    }
}
