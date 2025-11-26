package com.example.clubservice.service;

import com.example.clubservice.dto.request.LoginRequest;
import com.example.clubservice.dto.response.ClubResponse;
import com.example.clubservice.dto.response.TokenResponse;
import com.example.clubservice.entity.Club;
import com.example.clubservice.entity.Member;
import com.example.clubservice.enums.Authority;
import com.example.clubservice.exception.CustomException;
import com.example.clubservice.exception.error.ErrorCode;
import com.example.clubservice.repository.ClubRepository;
import com.example.clubservice.repository.MemberRepository;
import com.example.clubservice.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member testMember;
    private String rawPassword = "password123";

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .phone("010-1234-5678")
                .name("홍길동")
                .password(passwordEncoder.encode(rawPassword))
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(testMember);
    }

    @DisplayName("올바른 전화번호와 비밀번호로 로그인하면 토큰이 발급된다")
    @Test
    void login_Success() {
        // given
        LoginRequest request = new LoginRequest(testMember.getPhone(), rawPassword);

        // when
        TokenResponse result = authService.login(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.grantType()).isEqualTo("Bearer");
        assertThat(result.accessToken()).isNotEmpty();
        assertThat(result.refreshToken()).isNotEmpty();
        assertThat(result.accessTokenExpiresIn()).isPositive();
    }

    @DisplayName("로그인하면 RefreshToken이 DB에 저장된다")
    @Test
    void login_RefreshTokenSaved() {
        // given
        LoginRequest request = new LoginRequest(testMember.getPhone(), rawPassword);

        // when
        TokenResponse result = authService.login(request);

        // then
        assertThat(refreshTokenRepository.findByKey(testMember.getPhone()))
                .isPresent()
                .hasValueSatisfying(refreshToken -> {
                    assertThat(refreshToken.getKey()).isEqualTo(testMember.getPhone());
                    assertThat(refreshToken.getValue()).isEqualTo(result.refreshToken());
                });
    }

    @DisplayName("존재하지 않는 전화번호로 로그인하면 예외가 발생한다")
    @Test
    void login_UserNotFound_ThrowsException() {
        // given
        LoginRequest request = new LoginRequest("010-9999-9999", rawPassword);

        // when
        Exception exception = assertThrows(Exception.class, () -> {
            authService.login(request);
        });

        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> {
                    CustomException customException = (CustomException) e;
                    assertThat(customException.getError()).isEqualTo(ErrorCode.USER_NOT_FOUND);
                });
    }

    @DisplayName("잘못된 비밀번호로 로그인하면 예외가 발생한다")
    @Test
    void login_WrongPassword_ThrowsException() {
        // given
        LoginRequest request = new LoginRequest(testMember.getPhone(), "wrongPassword");

        // when & then
        assertThrows(Exception.class, () -> {
            authService.login(request);
        });
    }

    @DisplayName("null 전화번호로 로그인하면 예외가 발생한다")
    @Test
    void login_NullPhone_ThrowsException() {
        // given
        LoginRequest request = new LoginRequest(null, rawPassword);

        // when & then
        assertThrows(Exception.class, () -> {
            authService.login(request);
        });
    }

    @DisplayName("null 비밀번호로 로그인하면 예외가 발생한다")
    @Test
    void login_NullPassword_ThrowsException() {
        // given
        LoginRequest request = new LoginRequest(testMember.getPhone(), null);

        // when & then
        assertThrows(Exception.class, () -> {
            authService.login(request);
        });
    }

    @DisplayName("빈 문자열 전화번호로 로그인하면 예외가 발생한다")
    @Test
    void login_EmptyPhone_ThrowsException() {
        // given
        LoginRequest request = new LoginRequest("", rawPassword);

        // when & then
        assertThrows(Exception.class, () -> {
            authService.login(request);
        });
    }

    @DisplayName("로그인하면 RefreshToken이 저장되거나 갱신된다")
    @Test
    void login_RefreshTokenSavedOrUpdated() {
        // given
        LoginRequest request = new LoginRequest(testMember.getPhone(), rawPassword);

        // when
        authService.login(request);
        authService.login(request);

        // then
        assertThat(refreshTokenRepository.findByKey(testMember.getPhone()))
                .isPresent();
    }
}