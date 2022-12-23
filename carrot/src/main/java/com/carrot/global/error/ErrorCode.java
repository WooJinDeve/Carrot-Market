package com.carrot.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {
    //USER
    EMAIL_VALIDATION_ERROR(BAD_REQUEST, "이메일 형식이 잘못되었습니다."),
    NICKNAME_LENGTH_VALIDATION_ERROR(BAD_REQUEST, "닉네임의 길이가 15을 초과했습니다."),

    //OAUTH2
    OAUTH2_TYPE_VALIDATION_ERROR(UNAUTHORIZED, "지원하지않는 소셜로그인입니다."),;


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
