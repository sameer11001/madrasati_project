package com.webapp.madrasati.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerApp {
    private static final Logger logger = LoggerFactory.getLogger(LoggerApp.class);

    // just a utility class
    private LoggerApp() {
        throw new IllegalStateException("Utility class");
    }

    public static void debug(String message, Object... args) {
        logger.debug(formatMessage(message, args));
    }

    public static void info(String message, Object... args) {
        logger.info(formatMessage(message, args));
    }

    public static void warn(String message, Object... args) {
        logger.warn(formatMessage(message, args));
    }

    public static void error(String message, Object... args) {
        logger.error(formatMessage(message, args));
    }

    public static void error(Throwable throwable, String message, Object... args) {
        logger.error(formatMessage(message, args), throwable);
    }

    private static String formatMessage(String message, Object... args) {
        if (args.length > 0) {
            return String.format(message, args);
        } else {
            return message;
        }
    }
}
