package com.account.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiErrorResponse {
	
	private int status;
    private String errorCode;
    private String errorMessage;

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