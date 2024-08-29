package com.webapp.madrasati.core.error;

public class BadRequestException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = """
            Bad Request!
            """;

    public BadRequestException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
