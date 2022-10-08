package com.vivahspotuser.exception;

public class UserAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String message;

	public UserAlreadyExistException() {
	}

	public UserAlreadyExistException(String msg) {
		super(msg);
		this.message = msg;
	}
}
