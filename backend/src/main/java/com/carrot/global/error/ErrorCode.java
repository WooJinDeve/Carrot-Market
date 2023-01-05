package com.carrot.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    //USER
    EMAIL_VALIDATION_ERROR(BAD_REQUEST, "이메일 형식이 잘못되었습니다."),
    NICKNAME_LENGTH_VALIDATION_ERROR(BAD_REQUEST, "닉네임의 길이가 15을 초과했습니다."),
    USER_NOTFOUND_ERROR(NOT_FOUND, "존재하지 않는 회원입니다."),
    USER_REGION_MAX_ERROR(BAD_REQUEST, "지역정보는 최대 2개까지 저장할 수 있습니다."),
    USER_REGION_NOTFOUND_ERROR(NOT_FOUND, "존재하지 않는 회원의 지역정보입니다."),
    USER_REGION_VALIDATION_ERROR(UNAUTHORIZED, "해당 지역정보에 관한 권한이 없습니다."),
    USER_REGION_DUPLICATION_ERROR(BAD_REQUEST, "같은 지역을 2번 이상 저장할 수 없습니다."),

    //REGION
    REGION_LENGTH_VALIDATION_ERROR(BAD_REQUEST, "지역명은 50자를 초과할 수 없습니다."),
    REGION_NOTFOUND_ERROR(NOT_FOUND, "존재하지 않는 지역입니다."),

    //POST
    POST_TITLE_VALIDATION_ERROR(BAD_REQUEST, "제목은 최대 50자 입니다."),
    POST_NOTFOUND_ERROR(NOT_FOUND, "존재하지 않는 포스팅입니다."),
    POST_VALIDATION_ERROR(UNAUTHORIZED, "해당 게시물에 관한 권한이 없습니다."),
    POST_HITS_OVERFLOW_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "조회수가 최대값을 초과했습니다."),
    POST_CATEGORY_NOTFOUND_ERROR(NOT_FOUND, "존재하지 않는 카테고리입니다."),

    //POST LIKE
    POST_LIKE_VALIDATION_ERROR(BAD_REQUEST, "이미 좋아요를 누르셨습니다."),
    POST_LIKE_NOTFOUND_ERROR(NOT_FOUND, "저장된 좋아요 정보가 없습니다."),


    //ARTICLE
    ARTICLE_SENTENCE_LENGTH_ERROR(BAD_REQUEST, "최대 작성 범위가 초과했거나 문자를 입력하지 않았습니다."),
    ARTICLE_NOTFOUND_ERROR(NOT_FOUND, "존재하지 않는 댓글입니다."),
    ARTICLE_VALIDATION_ERROR(UNAUTHORIZED, "해당 댓글에 관한 권한이 없습니다."),

    //OAUTH2
    OAUTH2_TYPE_VALIDATION_ERROR(UNAUTHORIZED, "지원하지않는 소셜로그인입니다."),

    //JWT
    TOKEN_VALIDATION_ERROR(UNAUTHORIZED, "회원의 리프레시 토큰이 아닙니다."),
    HEADER_NOTFOUND_ERROR(BAD_REQUEST, "헤더가 존재하지 않습니다."),


    //SERVER
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),

    //CLIENT
    CLIENT_PARAMETER_ERROR(BAD_REQUEST, "사용자의 요청정보가 잘못되었습니다");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
