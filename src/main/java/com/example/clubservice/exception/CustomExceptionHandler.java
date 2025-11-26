package com.example.clubservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorRes> handleException(CustomException ex) {
        ErrorRes res = ErrorRes.builder()
                .status(ex.getError().getStatus().value())
                .message(ex.getError().getMessage())
                .build();
        return new ResponseEntity<ErrorRes>(res, ex.getError().getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorRes> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorRes res = ErrorRes.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("비밀번호가 일치하지 않습니다.")
                .build();
        return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
    }
}
