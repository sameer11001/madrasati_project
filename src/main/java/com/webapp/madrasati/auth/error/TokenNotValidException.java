package com.webapp.madrasati.auth.error;

public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException(String message) {
        super(MESSAGE + message);
    }

    public TokenNotValidException() {
        super(MESSAGE);
    }

    public static final String MESSAGE = "Token is not valid. ";

}
