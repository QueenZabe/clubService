package com.example.clubservice.presentation.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorRes {
    private String code;
    private String message;
}