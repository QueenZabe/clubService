package com.example.clubservice.dto.response;

import lombok.Getter;

public record TokenResponse(
        String grantType,
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken
) {
    public static TokenResponse from(
            String grantType,
            String accessToken,
            Long expiresIn,
            String refreshToken
    ) {
        return new TokenResponse(
                grantType,
                accessToken,
                expiresIn,
                refreshToken
        );
    }
}