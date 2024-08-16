package com.webapp.madrasati.core.error;

public class AlreadyExistException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = """
            Already Exist!
            """;

    public AlreadyExistException(String message) {
        super(message + " " + DEFAULT_MESSAGE);
    }

    public AlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

}
