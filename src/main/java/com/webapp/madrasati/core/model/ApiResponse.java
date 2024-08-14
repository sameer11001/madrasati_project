package com.webapp.madrasati.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final int status;

    private final boolean success;

    private final String message;

    private final T data;

    public static final ApiResponse<Void> successWithNoData = ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .success(true)
            .build();

    public static <T> ApiResponse<T> success(T data) {
        return success(data, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status(Objects.requireNonNull(status, "HttpStatus must not be null").value())
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> errorServer(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status(Objects.requireNonNull(status, "HttpStatus must not be null").value())
                .success(false)
                .message(Objects.requireNonNull(message, "Error message must not be null"))
                .build();
    }
}
