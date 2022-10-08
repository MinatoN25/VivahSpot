package com.stackroute.payment.exception;

public class NotEligibleForPaymentException extends RuntimeException {
	private final String errorMessage;
	private static final long serialVersionUID = 1L;

	public NotEligibleForPaymentException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
