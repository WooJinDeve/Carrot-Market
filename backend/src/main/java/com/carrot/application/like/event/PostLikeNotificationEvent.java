package com.carrot.application.like.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeNotificationEvent {
    private Long senderId;
    private Long postId;
    private Long targetId;
}
