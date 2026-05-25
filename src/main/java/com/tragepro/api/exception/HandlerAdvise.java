package com.tragepro.api.exception;

import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.exception.model.AppErrorDto;
import com.tragepro.api.exception.model.ServerErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(AppErrorDto.builder()
                        .errorCode(ErrorType.INVALID_PARAMETER.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .message(fieldErrors)
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        String violations = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(AppErrorDto.builder()
                        .errorCode(ErrorType.INVALID_PARAMETER.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .message(violations)
                        .build());
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<AppErrorDto> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException ex) {
        return ResponseEntity.status(403)
                .body(AppErrorDto.builder()
                        .errorCode(ErrorType.ACCESS_DENIED.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppErrorDto> handleRuntime(RuntimeException runtimeException) {
        ErrorType errorType = ErrorType.INTERNAL_ERROR;
        return ResponseEntity.status(errorType.getCode())
                .body(AppErrorDto.builder()
                        .errorCode(errorType.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .message(runtimeException.getLocalizedMessage())
                        .build());
    }
}
