package com.example.clubservice.dto.request;

public record TokenRequest(
        String accessToken,
        String refreshToken
) {
}
