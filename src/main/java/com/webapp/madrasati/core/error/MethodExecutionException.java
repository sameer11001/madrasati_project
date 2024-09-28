package com.webapp.madrasati.core.error;

public class MethodExecutionException extends RuntimeException {
    public MethodExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}