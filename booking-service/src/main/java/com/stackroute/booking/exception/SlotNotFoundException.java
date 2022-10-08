package com.stackroute.booking.exception;

public class SlotNotFoundException extends RuntimeException {
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public SlotNotFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
