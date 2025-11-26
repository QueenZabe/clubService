package com.example.clubservice.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements CustomErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동아리를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾지 못하였습니다."),

    ALREADY_EXISTS_USER(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다."),
    ALREADY_EXISTS_CLUB(HttpStatus.CONFLICT, "해당 이름의 동아리가 이미 존재합니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),
    LOGGED_OUT_USER(HttpStatus.UNAUTHORIZED, "로그아웃된 사용자입니다."),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "토큰의 유저 정보가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}