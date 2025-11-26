package com.example.clubservice.service;

import com.example.clubservice.dto.request.LoginRequest;
import com.example.clubservice.dto.request.MemberCreateRequest;
import com.example.clubservice.dto.request.TokenRequest;
import com.example.clubservice.dto.response.TokenResponse;
import com.example.clubservice.entity.Member;
import com.example.clubservice.entity.RefreshToken;
import com.example.clubservice.exception.CustomException;
import com.example.clubservice.exception.error.ErrorCode;
import com.example.clubservice.repository.MemberRepository;
import com.example.clubservice.repository.RefreshTokenRepository;
import com.example.clubservice.usecase.AuthDetails;
import com.example.clubservice.usecase.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public void signup(MemberCreateRequest request) {
        if (memberRepository.existsById(request.phone())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_USER);
        }

        Member member = request.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findById(request.phone())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        AuthDetails authDetails = new AuthDetails(member);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                authDetails,
                authentication.getCredentials(),
                authentication.getAuthorities()
        );

        TokenResponse tokenResponse = tokenProvider.generateTokenResponse(newAuth);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(member.getPhone())
                .value(tokenResponse.refreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenResponse;
    }

    public TokenResponse reissue(TokenRequest request) {
        if (!tokenProvider.validateToken(request.refreshToken())) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication =
                tokenProvider.getAuthentication(request.accessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGGED_OUT_USER));

        if (!refreshToken.getValue().equals(request.refreshToken())) {
            throw new CustomException(ErrorCode.TOKEN_MISMATCH);
        }

        TokenResponse tokenResponse = tokenProvider.generateTokenResponse(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenResponse.refreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenResponse;
    }
}