package com.example.bdm.exception;
public class ActivationTokenGenerationException extends RuntimeException {

    public ActivationTokenGenerationException(String message) {
        super(message);
    }

    public ActivationTokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivationTokenGenerationException() {
        super("Failed to generate activation token on register");
    }
}
