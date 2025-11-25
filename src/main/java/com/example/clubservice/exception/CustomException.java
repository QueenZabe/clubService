package com.example.clubservice.exception;

import com.example.clubservice.exception.error.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final CustomErrorCode error;
}
