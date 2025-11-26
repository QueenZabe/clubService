package com.example.clubservice.dto.request;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public record LoginRequest(
        String phone,
        String password
) {
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(phone, password);
    }
}
