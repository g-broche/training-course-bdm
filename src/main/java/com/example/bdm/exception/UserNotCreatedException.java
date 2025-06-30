package com.example.bdm.exception;
public class UserNotCreatedException extends RuntimeException {

    public UserNotCreatedException(String message) {
        super(message);
    }

    public UserNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotCreatedException() {
        super("A new user was not created due to an error");
    }
}
