package com.stackroute.booking.exception;

public class DateBeforeCurrentDateException extends RuntimeException {

	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public DateBeforeCurrentDateException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
