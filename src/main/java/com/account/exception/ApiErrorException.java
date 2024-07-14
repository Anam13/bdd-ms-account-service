package com.account.exception;

public class ApiErrorException extends RuntimeException {


	private static final long serialVersionUID = 471225503469436032L;
	private int statusCode; 
	private String errorCode; 
	private String errorMessage; 
	
	public ApiErrorException() {
	}

	public ApiErrorException(int statusCode, String errorCode, String errorMessage) {
		super(errorMessage);
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
