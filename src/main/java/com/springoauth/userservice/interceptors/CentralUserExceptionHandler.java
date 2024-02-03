package com.springoauth.userservice.interceptors;

import com.springoauth.userservice.dtos.DateTimeDTO;
import com.springoauth.userservice.dtos.SessionDTO;
import com.springoauth.userservice.exceptions.IllegalUserFormatException;
import com.springoauth.userservice.exceptions.IllegalUserSessionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class CentralUserExceptionHandler {
    @ExceptionHandler({IllegalUserFormatException.class})
    public ResponseEntity<String> handleFormatException(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({IllegalUserSessionException.class})
    public ResponseEntity<List<DateTimeDTO>> handleSessionsException(IllegalUserSessionException e){
        return new ResponseEntity<>(e.getListOfLogins(),HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }
}
