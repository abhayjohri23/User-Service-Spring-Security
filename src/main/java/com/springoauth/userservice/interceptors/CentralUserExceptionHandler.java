package com.springoauth.userservice.interceptors;

import com.springoauth.userservice.exceptions.IllegalUserFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CentralUserExceptionHandler {
    @ExceptionHandler({IllegalUserFormatException.class})
    public ResponseEntity<String> handleException(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
