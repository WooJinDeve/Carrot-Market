package com.carrot.application.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    NEW_COMMENT_ON_POST("작성하신 게시물에 댓글을 남겼습니다."),
    NEW_REPLY_ON_COMMENT("작성하신 댓글에 답글을 남겼습니다."),
    NEW_LIKE_ON_POST("사용자님의 게시물을 좋아합니다."),
    CREATE_POST("포스팅이 정상적으로 등록되었습니다.");

    private final String alarmText;
}
