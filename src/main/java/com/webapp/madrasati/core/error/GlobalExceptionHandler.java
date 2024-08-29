package com.webapp.madrasati.core.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.model.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        LoggerApp.error("Resource not found exception: ", ex);
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistException.class)
    public ApiResponse<String> handleAlreadyExistException(AlreadyExistException ex) {
        LoggerApp.error("Already exist exception: ", ex);
        return createErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        LoggerApp.error("User not found: ", ex);
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class })
    public ApiResponse<String> handleAuthenticationException(Exception ex) {
        LoggerApp.error("Authentication exception: ", ex);
        return createErrorResponse("Authentication failed", HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<String> handleAccessDeniedException(AccessDeniedException ex) {
        LoggerApp.error("Access denied: ", ex);
        return createErrorResponse("Access denied", HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
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
    public ApiResponse<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        LoggerApp.error("Malformed JSON request: ", ex);
        return createErrorResponse("Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiResponse<String> handleBadRequestException(BadRequestException ex) {
        LoggerApp.error("Bad request: ", ex);
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    private ApiResponse<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        LoggerApp.error("Resource not found: ", ex);
        return createErrorResponse("Resource not found" + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ApiResponse<String> handleInternalServerErrorException(InternalServerErrorException ex) {
        LoggerApp.error("Internal server error: ", ex);
        return createErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleUnexpectedException(Exception ex) {
        LoggerApp.error("Unexpected error occurred: ", ex);
        return createErrorResponse("An unexpected error occurred " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T> ApiResponse<T> createErrorResponse(String message, HttpStatus status) {
        return ApiResponse.error(message, status);
    }

    private <T> ApiResponse<T> createErrorResponseWithData(String message, HttpStatus status, T data) {
        return ApiResponse.errorWithData(message, status, data);
    }
}
// handle route 404