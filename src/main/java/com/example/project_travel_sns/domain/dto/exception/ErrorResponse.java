package com.example.project_travel_sns.domain.dto.exception;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ErrorResponse {
    private String ErrorCode;
    private String message;

    public static ErrorResponse of(String errorCode, String message) {
        return ErrorResponse.builder()
                .ErrorCode(errorCode)
                .message(message)
                .build();
    }
}
