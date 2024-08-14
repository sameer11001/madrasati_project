package com.webapp.madrasati.core.error;

public class InternalServerErrorException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = """
            Internal Server Error!
            """;

    public InternalServerErrorException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    public InternalServerErrorException() {
        super(DEFAULT_MESSAGE);
    }

}
