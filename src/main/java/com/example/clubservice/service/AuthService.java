package com.example.clubservice.service;

import com.example.clubservice.dto.request.LoginRequest;
import com.example.clubservice.dto.request.MemberCreateRequest;
import com.example.clubservice.dto.request.TokenRequest;
import com.example.clubservice.dto.response.TokenResponse;

public interface AuthService {
    void signup(MemberCreateRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse reissue(TokenRequest request);

}
