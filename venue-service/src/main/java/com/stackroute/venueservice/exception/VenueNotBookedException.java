package com.stackroute.venueservice.exception;

public class VenueNotBookedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public VenueNotBookedException(String message) {
		super(message);

	}
}
