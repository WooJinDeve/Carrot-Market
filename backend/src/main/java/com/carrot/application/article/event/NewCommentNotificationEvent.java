package com.carrot.application.article.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewCommentNotificationEvent {


    private Long senderId;
    private Long receiverId;
    private Long commentId;

}
