package com.stackroute.booking.exception;

public class UnauthorizedException extends RuntimeException{
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
