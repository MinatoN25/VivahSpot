package com.vivahspotuser.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String message;

	public UserNotFoundException() {
	}

	public UserNotFoundException(String msg) {
		super(msg);
		this.message = msg;
	}
}
