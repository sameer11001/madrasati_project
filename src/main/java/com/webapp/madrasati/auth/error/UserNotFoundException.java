package com.webapp.madrasati.auth.error;

public class UserNotFoundException extends RuntimeException {

    private static String DEFAULT_MESSAGE = "User not found!";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
