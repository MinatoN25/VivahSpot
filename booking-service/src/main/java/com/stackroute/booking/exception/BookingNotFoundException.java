package com.stackroute.booking.exception;

public class BookingNotFoundException extends RuntimeException{

	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public BookingNotFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
