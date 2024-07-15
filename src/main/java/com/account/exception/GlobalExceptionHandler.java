package com.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ApiErrorResponse> handleApiErrorException(ApiErrorException ex, WebRequest request) {
        log.error("GlobalExceptionHandler : ApiErrorException ()");

        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getErrorCode(),
                ex.getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleApiErrorException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("GlobalExceptionHandler : MethodArgumentNotValidException ()");
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ex.getTitleMessageCode(),
                ex.getTitleMessageCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalExceptions(Exception ex, WebRequest request) {
        log.error("GlobalExceptionHandler : Exception ()");

        ApiErrorResponse errorDetails = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
