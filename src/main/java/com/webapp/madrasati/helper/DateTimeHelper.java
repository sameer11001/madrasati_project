package com.webapp.madrasati.helper;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import io.micrometer.common.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateTimeHelper {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeHelper instance = new DateTimeHelper();
    public static final DateTimeHelper Instance = instance;

    public String formatDate(LocalDate date) {
        return formatDate(date, DEFAULT_FORMATTER);
    }

    public String formatDate(LocalDate date, DateTimeFormatter formatter) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return formatter.format(date);
    }

    public LocalDate parseDate(String dateString) {
        return parseDate(dateString, DEFAULT_FORMATTER);
    }

    public LocalDate parseDate(String dateString, DateTimeFormatter formatter) {
        if (StringUtils.isBlank(dateString)) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new InternalServerErrorException("Invalid date format: " + dateString, e);
        }
    }

    public long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
