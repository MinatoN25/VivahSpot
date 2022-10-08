package com.stackroute.payment.exception;

public class NotEligibleForRefundException extends RuntimeException {
	private final String errorMessage;
	private static final long serialVersionUID = 1L;

	public NotEligibleForRefundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
