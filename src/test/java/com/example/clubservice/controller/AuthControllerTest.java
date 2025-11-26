package com.example.clubservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clubservice.dto.request.LoginRequest;
import com.example.clubservice.entity.Member;
import com.example.clubservice.enums.Authority;
import com.example.clubservice.repository.MemberRepository;
import com.example.clubservice.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Member testMember;
    private String rawPassword = "password123";

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        // 테스트용 회원 데이터 생성
        testMember = Member.builder()
                .phone("010-1234-5678")
                .name("홍길동")
                .password(passwordEncoder.encode(rawPassword))
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(testMember);
    }

    @AfterEach
    void cleanUp() {
        refreshTokenRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("POST, /auth/login 요청 시 올바른 자격증명으로 200 OK와 토큰이 반환된다")
    @Test
    public void login_Success() throws Exception {
        // given
        final String url = "/auth/login";
        LoginRequest request = new LoginRequest(testMember.getPhone(), rawPassword);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.grantType").value("Bearer"))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.accessTokenExpiresIn").exists());
    }

    @DisplayName("POST, /auth/login 요청 시 존재하지 않는 전화번호로 404 NotFound가 반환된다")
    @Test
    public void login_UserNotFound_Fail() throws Exception {
        // given
        final String url = "/auth/login";
        LoginRequest request = new LoginRequest("010-9999-9999", rawPassword);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("POST, /auth/login 요청 시 잘못된 비밀번호로 401 Unauthorized가 반환된다")
    @Test
    public void login_WrongPassword_Fail() throws Exception {
        // given
        final String url = "/auth/login";
        LoginRequest request = new LoginRequest(testMember.getPhone(), "wrongPassword");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("POST, /auth/login 요청 시 잘못된 JSON 형식으로 400 Bad Request가 반환된다")
    @Test
    public void login_InvalidJson_Fail() throws Exception {
        // given
        final String url = "/auth/login";
        final String invalidJson = "{invalid json}";

        // when
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));

        // then
        result
                .andExpect(status().isBadRequest());
    }
}