package com.example.clubservice.handler;

import com.example.clubservice.exception.ErrorResponse;
import com.example.clubservice.exception.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        String requestURI = request.getRequestURI();
        log.error("접근 거부 - URI: {}, 에러: {}", requestURI, accessDeniedException.getMessage());

        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}