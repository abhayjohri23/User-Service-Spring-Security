package com.springoauth.userservice.exceptions;

public class IllegalUserFormatException extends Exception{
    public IllegalUserFormatException(String message){
        super(message);
    }
}
