package com.example.bdm.exception;
public class RegistrationActivationFailedException extends RuntimeException {

    public RegistrationActivationFailedException(String message) {
        super(message);
    }

    public RegistrationActivationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationActivationFailedException() {
        super("Invalid registration token");
    }
}
