package com.webapp.madrasati.auth.error;

public class NoTokenFoundException extends RuntimeException {

    public NoTokenFoundException(String message) {
        super(message);
    }
    
}
