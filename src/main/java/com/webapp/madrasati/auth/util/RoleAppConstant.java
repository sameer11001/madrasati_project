package com.webapp.madrasati.auth.util;

import lombok.Getter;

@Getter
public enum RoleAppConstant {

    ADMIN("ROLE_ADMIN"), STUDENT("ROLE_STUDENT"), SMANAGER("ROLE_SCHOOL_MANAGER");

    private final String string;

    RoleAppConstant(String string) {

        this.string = string;
    }

}
