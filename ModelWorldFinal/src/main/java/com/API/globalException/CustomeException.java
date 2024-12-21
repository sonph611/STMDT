package com.API.globalException;

public class CustomeException extends RuntimeException  {
	private String message ;
	
	public CustomeException() {
		super();
	}
	
	public CustomeException(String message) {
		super();
		this.message=message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
