package com.carrot.application.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    NEW_COMMENT_ON_POST("new comment!"),
    NEW_LIKE_ON_POST("new post!"),
    NEW_CHAT_ON_POST("new chatting!"),;

    private final String alarmText;
}
