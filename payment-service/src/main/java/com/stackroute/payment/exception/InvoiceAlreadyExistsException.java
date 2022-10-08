package com.stackroute.payment.exception;

public class InvoiceAlreadyExistsException extends RuntimeException {
	private final String errorMessage;
	private static final long serialVersionUID = 1L;

	public InvoiceAlreadyExistsException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
