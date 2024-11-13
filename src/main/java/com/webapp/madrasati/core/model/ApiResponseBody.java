package com.webapp.madrasati.core.model;

import java.time.LocalDateTime;

import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "API Response Body wrapper")
public class ApiResponseBody<T> {

    @Schema(description = "Timestamp of the response", example = "2023-05-24T10:30:15")
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "HTTP status code", example = "200")
    private final int status;

    @Schema(description = "Indicates if the operation was successful (boolean value)", example = "true")
    private final boolean success;

    @Schema(description = "Response message", example = "Operation completed successfully")
    private final String message;

    @Schema(description = "Response data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public static final ApiResponseBody<Void> successWithNoData = ApiResponseBody.<Void>builder()
            .status(HttpStatus.NO_CONTENT.value())
            .success(true)
            .build();

    public static <T> ApiResponseBody<T> success(T data) {
        return success(data, null);
    }

    public static <T> ApiResponseBody<T> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static <T> ApiResponseBody<T> success(T data, String message, HttpStatus status) {
        return ApiResponseBody.<T>builder()
                .status(Objects.requireNonNull(status, "HttpStatus must not be null").value())
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseBody<T> errorServer(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResponseBody<T> error(String message, HttpStatus status) {
        return ApiResponseBody.<T>builder()
                .status(Objects.requireNonNull(status, "HttpStatus must not be null").value())
                .success(false)
                .message(Objects.requireNonNull(message, "Error message must not be null"))
                .build();
    }

    public static <T> ApiResponseBody<T> errorWithData(String message, HttpStatus status, T data) {
        return ApiResponseBody.<T>builder()
                .status(Objects.requireNonNull(status, "HttpStatus must not be null").value())
                .success(false)
                .message(Objects.requireNonNull(message, "Error message must not be null"))
                .data(data)
                .build();
    }
}
