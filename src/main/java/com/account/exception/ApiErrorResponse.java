package com.account.exception;


public class ApiErrorResponse {
	
	private int status;
    private String errorCode;
    private String errorMessage;
    
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public ApiErrorResponse() {
	}
	public ApiErrorResponse(int status, String errorCode, String errorMessage) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "ApiErrorResponse [status=" + status + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ "]";
	}
	

}