package com.carrot.application.notification.event;

import com.carrot.application.notification.domain.NotificationArgs;
import com.carrot.application.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    private Long receiverId;
    private NotificationType type;
    private NotificationArgs args;
}
