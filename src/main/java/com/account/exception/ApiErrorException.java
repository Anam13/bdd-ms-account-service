package com.account.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ApiErrorException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = 471225503469436032L;

    private final int statusCode;
    private final String errorCode;
    private final String errorMessage;

    public ApiErrorException(int statusCode, String errorCode, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
