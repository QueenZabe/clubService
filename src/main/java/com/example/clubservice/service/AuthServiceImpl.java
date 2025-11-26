package com.example.clubservice.service;

import com.example.clubservice.dto.request.LoginRequest;
import com.example.clubservice.dto.request.MemberCreateRequest;
import com.example.clubservice.dto.request.TokenRequest;
import com.example.clubservice.dto.response.TokenResponse;
import com.example.clubservice.entity.Member;
import com.example.clubservice.entity.RefreshToken;
import com.example.clubservice.repository.MemberRepository;
import com.example.clubservice.repository.RefreshTokenRepository;
import com.example.clubservice.usecase.AuthDetails;
import com.example.clubservice.usecase.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    // 회원가입
    public void signup(MemberCreateRequest request) {

        if (memberRepository.existsById(request.phone())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = request.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    // 로그인
    public TokenResponse login(LoginRequest request) {
        System.out.println("========================================");
        System.out.println("로그인 시작");
        System.out.println("========================================");

        try {
            // 1) uid + password → AuthenticationToken 생성
            System.out.println("1단계: AuthenticationToken 생성");
            System.out.println("  - uid: " + request.phone());
            System.out.println("  - password 길이: " + (request.password() != null ? request.password().length() : "null"));

            UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();
            System.out.println("  ✓ AuthenticationToken 생성 완료");

            // 2) DB 검증 → CustomUserDetailsService.loadUserByUsername 실행됨
            System.out.println("\n2단계: DB 검증 시작");
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            System.out.println("  ✓ 인증 성공");
            System.out.println("  - Principal: " + authentication.getPrincipal().getClass().getSimpleName());
            System.out.println("  - Authorities: " + authentication.getAuthorities());

            // 3) Member 조회
            System.out.println("\n3단계: Member 조회");
            Member member = memberRepository.findById(request.phone())
                    .orElseThrow(() -> {
                        System.err.println("  ✗ Member 조회 실패: " + request.phone());
                        return new RuntimeException("사용자를 찾을 수 없습니다.");
                    });
            System.out.println("  ✓ Member 조회 성공");
            System.out.println("  - uid: " + member.getPhone());
            System.out.println("  - name: " + member.getName());
            System.out.println("  - password (암호화): " + member.getPassword().substring(0, 20) + "...");

            // 4) AuthDetails 기반 Authentication 재구성
            System.out.println("\n4단계: Authentication 재구성");
            AuthDetails authDetails = new AuthDetails(member);
            System.out.println("  - AuthDetails username: " + authDetails.getUsername());
            System.out.println("  - AuthDetails password: " + authDetails.getPassword().substring(0, 20) + "...");

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    authDetails,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
            System.out.println("  ✓ 새로운 Authentication 생성 완료");

            // 5) JWT 생성
            System.out.println("\n5단계: JWT 생성");
            TokenResponse tokenRes = tokenProvider.generateTokenResponse(newAuth);
            System.out.println("  ✓ JWT 생성 완료");
            System.out.println("  - grantType: " + tokenRes.grantType());
            System.out.println("  - accessToken 존재: " + (tokenRes.accessToken() != null && !tokenRes.accessToken().isEmpty()));
            System.out.println("  - accessToken 길이: " + (tokenRes.accessToken() != null ? tokenRes.accessToken().length() : 0));
            System.out.println("  - refreshToken 존재: " + (tokenRes.refreshToken() != null && !tokenRes.refreshToken().isEmpty()));
            System.out.println("  - refreshToken 길이: " + (tokenRes.refreshToken() != null ? tokenRes.refreshToken().length() : 0));
            System.out.println("  - expiresIn: " + tokenRes.accessTokenExpiresIn());

            // 6) Refresh Token 저장
            System.out.println("\n6단계: Refresh Token 저장");
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(member.getPhone())
                    .value(tokenRes.refreshToken())
                    .build();

            refreshTokenRepository.save(refreshToken);
            System.out.println("  ✓ Refresh Token 저장 완료");
            System.out.println("  - key: " + refreshToken.getKey());

            // 7) 토큰 반환
            System.out.println("\n7단계: 토큰 반환");
            System.out.println("  ✓ TokenRes 객체 반환");
            System.out.println("========================================");
            System.out.println("로그인 완료");
            System.out.println("========================================\n");

            return tokenRes;

        } catch (AuthenticationException e) {
            System.err.println("\n========================================");
            System.err.println("인증 실패 (AuthenticationException)");
            System.err.println("========================================");
            System.err.println("에러 타입: " + e.getClass().getSimpleName());
            System.err.println("에러 메시지: " + e.getMessage());
            e.printStackTrace();
            System.err.println("========================================\n");
            throw e;

        } catch (Exception e) {
            System.err.println("\n========================================");
            System.err.println("예상치 못한 에러 발생");
            System.err.println("========================================");
            System.err.println("에러 타입: " + e.getClass().getSimpleName());
            System.err.println("에러 메시지: " + e.getMessage());
            e.printStackTrace();
            System.err.println("========================================\n");
            throw e;
        }
    }

    // 토큰 재발급
    public TokenResponse reissue(TokenRequest request) {

        // 1) Refresh Token 유효성 검사
        if (!tokenProvider.validateToken(request.refreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2) Access Token에서 사용자 정보 추출
        Authentication authentication =
                tokenProvider.getAuthentication(request.accessToken());

        // 3) DB에 저장된 Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4) Refresh Token 일치 여부 체크
        if (!refreshToken.getValue().equals(request.refreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5) 새로운 JWT 발급
        TokenResponse tokenRes = tokenProvider.generateTokenResponse(authentication);

        // 6) Refresh Token 갱신
        RefreshToken newRefreshToken =
                refreshToken.updateValue(tokenRes.refreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 7) 최종 반환
        return tokenRes;
    }
}
