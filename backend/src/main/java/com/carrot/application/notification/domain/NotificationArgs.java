package com.carrot.application.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationArgs {

    //알람을 발생시킨 사람
    private Long senderId;
    //알람을 발생시킨 주체 ex) postId, likeId
    private Long targetId;
}
