package com.example.clubservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private int status;
    private String message;
    private T data;

    public static <T> Response<T> of(HttpStatus status, String message, T data) {
        return Response.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Response<T> ok(T data) {
        return Response.<T>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(data)
                .build();
    }

    public static Response<Void> ok(String message) {
        return Response.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> Response<T> created(T data) {
        return Response.<T>builder()
                .status(HttpStatus.CREATED.value())
                .message("created")
                .data(data)
                .build();
    }
}