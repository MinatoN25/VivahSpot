package com.stackroute.venueservice.exception;

public class VenueAlreadyExistException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public VenueAlreadyExistException(String message) {

		super(message);
	}

}
