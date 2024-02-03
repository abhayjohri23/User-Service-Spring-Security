package com.springoauth.userservice.exceptions;

import com.springoauth.userservice.dtos.DateTimeDTO;
import lombok.Getter;
import lombok.Setter;

import java.rmi.server.ExportException;
import java.util.List;

@Getter
@Setter
public class IllegalUserSessionException extends Exception {
    private List<DateTimeDTO> listOfLogins = null;
    public IllegalUserSessionException(String message){
        super(message);
    }
    public IllegalUserSessionException(String message, List<DateTimeDTO> lastLogins){
        super(message);
        listOfLogins = lastLogins;
    }
}
