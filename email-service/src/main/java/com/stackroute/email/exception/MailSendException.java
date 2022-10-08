package com.stackroute.email.exception;

public class MailSendException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public MailSendException(String message) {
        super(message);
    }
}