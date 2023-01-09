package com.example.project_travel_sns.configuration.security.exception;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.exception.ErrorResponse;
import com.example.project_travel_sns.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AccessDeniedManager implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error(ErrorCode.INVALID_PERMISSION.getMessage());
        response.setStatus(ErrorCode.INVALID_PERMISSION.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_PERMISSION.name(), ErrorCode.INVALID_TOKEN.getMessage());
        Response resultResponse = Response.of("ERROR", errorResponse);
        String json = objectMapper.writeValueAsString(resultResponse);
        response.getWriter().write(json);
    }
}
