package com.webapp.madrasati.auth.util;

public enum GenderConstant {
    MALE('M'), FEMALE('F');

    private final char code;

    GenderConstant(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static GenderConstant fromCode(char code) {
        for (GenderConstant gender : GenderConstant.values()) {
            if (gender.getCode() == code) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender code: " + code);
    }

}
