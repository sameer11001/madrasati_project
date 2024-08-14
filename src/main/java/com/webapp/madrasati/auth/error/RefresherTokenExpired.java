package com.webapp.madrasati.auth.error;

public class RefresherTokenExpired extends RuntimeException {

    public static final String MESSAGE = "Refresher token is expired. Please make a new login..!";

    public RefresherTokenExpired(String message) {
        super(message + " " + MESSAGE);
    }

    RefresherTokenExpired() {
    }
}
