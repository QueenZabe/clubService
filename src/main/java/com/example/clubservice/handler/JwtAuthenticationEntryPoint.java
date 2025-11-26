package com.example.clubservice.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String requestURI = request.getRequestURI();
        log.error("인증 실패 - URI: {}, 에러: {}", requestURI, authException.getMessage());

        if (requestURI.startsWith("/auth/signup") ||
                requestURI.startsWith("/auth/login") ||
                requestURI.startsWith("/auth/reissue")) {
            log.info("Public 경로이므로 401 반환하지 않음: {}", requestURI);
            return;
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}