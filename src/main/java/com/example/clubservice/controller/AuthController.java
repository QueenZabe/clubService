package com.example.clubservice.controller;

import com.example.clubservice.dto.request.LoginRequest;
import com.example.clubservice.dto.request.MemberCreateRequest;
import com.example.clubservice.dto.request.TokenRequest;
import com.example.clubservice.dto.response.Response;
import com.example.clubservice.dto.response.TokenResponse;
import com.example.clubservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public Response<String> signup(@RequestBody MemberCreateRequest request) {
        authService.signup(request);
        return Response.created("정상적으로 회원가입 되었습니다.");
    }

    @PostMapping("/login")
    public Response<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return Response.ok(token);
    }

    @PostMapping("/reissue")
    public Response<TokenResponse> reissue(@RequestBody TokenRequest request) {
        TokenResponse token = authService.reissue(request);
        return Response.ok(token);
    }
}