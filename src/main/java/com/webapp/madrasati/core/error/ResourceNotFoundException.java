package com.webapp.madrasati.core.error;

public class ResourceNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = """
            Resource not found!
            """;

    public ResourceNotFoundException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ResourceNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
