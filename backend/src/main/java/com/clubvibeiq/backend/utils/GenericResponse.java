package com.clubvibeiq.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<Error> errors;

    public static <T> GenericResponse<T> empty() {
        return success(null);
    }

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .message("SUCCESS")
                .data(data)
                .success(true)
                .errors(Collections.emptyList())
                .build();
    }

    public static <T> GenericResponse<T> error() {
        return GenericResponse.<T>builder()
                .message("ERROR")
                .success(false)
                .build();
    }

    public static <T> GenericResponse<T> error(String message, List<GenericResponse.Error> errors) {
        return GenericResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .build();
    }

    public static <T> GenericResponse<T> errorWithMessage(T data) {
        return GenericResponse.<T>builder()
                .message("ERROR")
                .data(data)
                .success(false)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Error { // Inner class for error details
        private String field;
        private String message;
        // Getters and setters (can be generated using Lombok)
    }
}
