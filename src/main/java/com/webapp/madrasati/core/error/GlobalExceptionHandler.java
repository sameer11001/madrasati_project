package com.webapp.madrasati.core.error;

import java.util.HashMap;
import java.util.Map;

import com.webapp.madrasati.auth.error.RefresherTokenExpired;
import com.webapp.madrasati.auth.error.TooManyRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.webapp.madrasati.auth.error.NoTokenFoundException;
import com.webapp.madrasati.auth.error.TokenNotValidException;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.model.ApiResponseBody;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponseBody<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        LoggerApp.error("Resource not found: ", ex.getMessage());
        return createErrorResponse("Resource not found :"+ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistException.class)
    public ApiResponseBody<String> handleAlreadyExistException(AlreadyExistException ex) {
        LoggerApp.error("Already exist exception: ", ex);
        return createErrorResponse("Conflict :" + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponseBody<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        LoggerApp.error("User not found: ", ex.getMessage());
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class, TokenNotValidException.class,
            NoTokenFoundException.class , ExpiredJwtException.class, RefresherTokenExpired.class})
    public ApiResponseBody<String> handleAuthenticationException(Exception ex) {
        LoggerApp.error("AUTHORIZED exception: ", ex.getMessage());
        return createErrorResponse("AUTHORIZED failed :" + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UnsupportedJwtException.class, MalformedJwtException.class, SignatureException.class})
    public ApiResponseBody<String> handleUnsupportedJwtException(Exception ex) {
        LoggerApp.error("Bad request : ", ex.getMessage());
        return createErrorResponse("Bad request :" + ex.getMessage() , HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponseBody<String> handleAccessDeniedException(AccessDeniedException ex) {
        LoggerApp.error("Access denied: ", ex.getMessage());
        return createErrorResponse("Access denied", HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseBody<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
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
    public ApiResponseBody<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        LoggerApp.error("Malformed JSON request: ", ex);
        return createErrorResponse("Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiResponseBody<String> handleBadRequestException(BadRequestException ex) {
        LoggerApp.error("Bad request: ", ex.getMessage());
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponseBody<String> handleInvalidRequestException(IllegalArgumentException ex) {
        LoggerApp.error("Bad request :", ex.getMessage());
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(TooManyRequestException.class)
    public ApiResponseBody<String> handleTooManyRequests(TooManyRequestException ex) {
        LoggerApp.error("Too Many Requests..");
        return createErrorResponse("Too Many Requests", HttpStatus.TOO_MANY_REQUESTS);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponseBody<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        LoggerApp.error("Resource not found: ", ex.getMessage());
        return createErrorResponse("Resource not found" + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerError.class)
    public ApiResponseBody<String> handleInternalServerErrorException(InternalServerError ex) {
        LoggerApp.error("Internal server error: ", ex.getMessage());
        return createErrorResponse("an error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponseBody<String> handleGlobalException(Exception ex) {
        LoggerApp.error("Internal server error: ", ex.getMessage());
        return createErrorResponse("an error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T> ApiResponseBody<T> createErrorResponse(String message, HttpStatus status) {
        return ApiResponseBody.error(message, status);
    }

    private <T> ApiResponseBody<T> createErrorResponseWithData(String message, HttpStatus status, T data) {
        return ApiResponseBody.errorWithData(message, status, data);
    }
}