package com.webapp.madrasati.school_group.util;

public enum GroupStorageConstant {
    CLASSNAME("group"),CATEGORY("post/");

    private String string;
    GroupStorageConstant(String string) {
        this.string = string;
    }
    public String getString() {
        return string;
    }
}
