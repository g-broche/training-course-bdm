package com.example.bdm.exception;

public class InvalidRegistrationInputException extends RuntimeException {
    public InvalidRegistrationInputException(String message) {
        super(message);
    }

    public InvalidRegistrationInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRegistrationInputException() {
        super("Invalid parameters given for registering");
    }
}
