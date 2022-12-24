package com.example.project_travel_sns.exception;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Response> appExceptionHandler(AppException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode().name(), e.getMessage());
        Response response = Response.of("ERROR", errorResponse);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }
}
