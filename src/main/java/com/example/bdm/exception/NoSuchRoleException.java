package com.example.bdm.exception;
public class NoSuchRoleException extends RuntimeException {

    public NoSuchRoleException(String message) {
        super(message);
    }

    public NoSuchRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchRoleException() {
        super("There is no such role");
    }
}
