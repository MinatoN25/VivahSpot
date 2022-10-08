package com.stackroute.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionHandlingController {



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMessageNotReadableException(HttpMessageNotReadableException e){
		return new ResponseEntity<>(e.getMostSpecificCause().getLocalizedMessage(),HttpStatus.BAD_REQUEST);

     }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
		return new ResponseEntity<>(e.getMostSpecificCause().getLocalizedMessage(),HttpStatus.BAD_REQUEST);

     }
    
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleRequestParameterException(MissingServletRequestParameterException e){
		return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);

     }
}