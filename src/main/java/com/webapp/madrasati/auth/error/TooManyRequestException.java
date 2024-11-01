package com.webapp.madrasati.auth.error;

public class TooManyRequestException extends RuntimeException{

    public TooManyRequestException(String message) {
        super(message);
    }
}
