package com.example.project_travel_sns.configuration.security.exception;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.exception.ErrorResponse;
import com.example.project_travel_sns.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationManager implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");
        if (exception != null) {
            log.error(ErrorCode.EXPIRE_TOKEN.getMessage());
            setResponse(ErrorCode.EXPIRE_TOKEN, response);
        } else {
            log.error(ErrorCode.INVALID_TOKEN.getMessage());
            setResponse(ErrorCode.INVALID_TOKEN, response);
        }
    }

    private void setResponse(ErrorCode errorCode, HttpServletResponse response) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Response resultResponse = Response.of("ERROR", ErrorResponse.of(errorCode.name(), errorCode.getMessage()));
        response.getWriter().write(objectMapper.writeValueAsString(resultResponse));
    }
}
