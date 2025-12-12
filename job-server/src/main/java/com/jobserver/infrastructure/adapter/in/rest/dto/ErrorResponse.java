package com.jobserver.infrastructure.adapter.in.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private String code;
    private LocalDateTime timestamp;
    private String path;

    public static ErrorResponse of(String message, String code, String path) {
        return ErrorResponse.builder()
                .message(message)
                .code(code)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
}
