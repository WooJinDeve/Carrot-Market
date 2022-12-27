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
    USER_NOTFOUND_ERROR(BAD_REQUEST, "존재하지 않는 회원입니다."),

    //REGION
    REGION_LENGTH_VALIDATION_ERROR(BAD_REQUEST, "지역명은 50자를 초과할 수 없습니다."),


    //OAUTH2
    OAUTH2_TYPE_VALIDATION_ERROR(UNAUTHORIZED, "지원하지않는 소셜로그인입니다."),

    //JWT
    TOKEN_VALIDATION_ERROR(UNAUTHORIZED, "회원의 리프레시 토큰이 아닙니다."),
    HEADER_NOTFOUND_ERROR(BAD_REQUEST, "헤더가 존재하지 않습니다."),


    //SERVER
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),;


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
