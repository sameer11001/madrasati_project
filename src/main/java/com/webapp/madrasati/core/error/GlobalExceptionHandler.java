package com.webapp.madrasati.core.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.model.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<String> handleResourceNotFoundException(ResourceNotFoundException ex,
            HttpServletRequest request) {
        LoggerApp.error("Resource not found exception: ", ex);
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ApiResponse<String> handleInternalServerErrorException(InternalServerErrorException ex,
            HttpServletRequest request) {
        LoggerApp.error("Internal server error: ", ex);
        return createErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class })
    public ApiResponse<String> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        LoggerApp.error("Authentication exception: ", ex);
        return createErrorResponse("Authentication failed", HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        LoggerApp.error("Validation error: {}", errors);
        return createErrorResponseWithData("Validation failed", HttpStatus.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        LoggerApp.error("Malformed JSON request: ", ex);
        return createErrorResponse("Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        LoggerApp.error("Unexpected error occurred: ", ex);
        return createErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T> ApiResponse<T> createErrorResponse(String message, HttpStatus status) {
        return ApiResponse.error(message, status);
    }

    private <T> ApiResponse<T> createErrorResponseWithData(String message, HttpStatus status, T data) {
        return ApiResponse.errorWithData(message, status, data);
    }
}