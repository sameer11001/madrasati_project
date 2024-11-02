package com.webapp.madrasati.util;

import org.bson.types.ObjectId;

import java.util.UUID;

public class AppUtilConverter {

    private static final AppUtilConverter instance = new AppUtilConverter();
    public static final AppUtilConverter Instance = instance;

    private AppUtilConverter() {
    }

    public ObjectId stringToObjectId(String id) {
        if (id == null || !ObjectId.isValid(id) || !id.matches("[0-9a-fA-F]{24}")) {
            throw new IllegalArgumentException("Invalid ObjectId string: " + id);
        }
        return new ObjectId(id);
    }

    public String objectIdToString(ObjectId objectId) {
        if (objectId == null) {
            throw new IllegalArgumentException("ObjectId cannot be null");
        }
        try {
            return objectId.toHexString();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ObjectId: " + objectId);
        }
    }

    public UUID stringToUUID(String uuidString) {
        if (uuidString == null) {
            throw new IllegalArgumentException("UUID string cannot be null");
        }
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID string: " + uuidString);
        }
    }

    public String uuidToString(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        try {
            return uuid.toString();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID: " + uuid);
        }
    }

    public Integer stringToInteger(String str) {
        if (str == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer string: " + str);
        }

    }

    public String integerToString(Integer integer) {
        if (integer == null) {
            throw new IllegalArgumentException("Integer cannot be null");
        }
        return integer.toString();
    }

}
