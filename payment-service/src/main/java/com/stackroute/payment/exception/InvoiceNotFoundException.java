package com.stackroute.payment.exception;

public class InvoiceNotFoundException extends RuntimeException {
	private final String errorMessage;
	private static final long serialVersionUID = 1L;

	public InvoiceNotFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
