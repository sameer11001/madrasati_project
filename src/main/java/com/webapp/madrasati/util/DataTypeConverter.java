package com.webapp.madrasati.util;

import org.bson.types.ObjectId;

import java.util.UUID;

public class DataTypeConverter {

    private static final DataTypeConverter instance = new DataTypeConverter();
    public static final DataTypeConverter Instance = instance;

    private DataTypeConverter() {
    }

    public ObjectId stringToObjectId(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid ObjectId string: " + id);
        }
        return new ObjectId(id);
    }

    public String objectIdToString(ObjectId objectId) {
        if (objectId == null) {
            throw new IllegalArgumentException("ObjectId cannot be null");
        }
        return objectId.toHexString();
    }

    public UUID stringToUUID(String uuidString) {
        if (uuidString == null) {
            throw new IllegalArgumentException("UUID string cannot be null");
        }
        return UUID.fromString(uuidString);
    }

    public String uuidToString(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        return uuid.toString();
    }

    public Integer stringToInteger(String str) {
        if (str == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        return Integer.valueOf(str);
    }

    public String integerToString(Integer integer) {
        if (integer == null) {
            throw new IllegalArgumentException("Integer cannot be null");
        }
        return integer.toString();
    }

}
