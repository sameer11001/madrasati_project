package com.webapp.madrasati.auth.util;

public enum RoleAppConstant {

    ADMIN("ROLE_ADMIN"), STUDENT("ROLE_STUDENT"), SMANAGER("ROLE_SCHOOL_MANAGER");

    private String string;

    RoleAppConstant(String string) {

        this.string = string;
    }

    public String getString() {
        return string;
    }

}
