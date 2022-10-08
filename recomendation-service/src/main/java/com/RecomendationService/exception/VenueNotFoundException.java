package com.RecomendationService.exception;

public class VenueNotFoundException extends RuntimeException {
	
	private final String errorMessage;

	private static final long serialVersionUID = 1L;

	public VenueNotFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
