package com.carrot.application.article.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewReplyNotificationEvent {

    private Long senderId;
    private Long receiverId;
    private Long replyId;

}
