package com.carrot.presentation.response;

import com.carrot.application.notification.domain.Notification;
import com.carrot.application.notification.domain.NotificationArgs;
import com.carrot.application.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public class NotificationResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyResponses{
        private List<NotifyResponse> notifications;
        private boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyResponse{
        private Long id;
        private NotificationType alarmType;
        private NotificationArgs args;
        private String text;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static NotifyResponse of(Notification notification){
            return NotifyResponse.builder()
                    .id(notification.getId())
                    .alarmType(notification.getType())
                    .args(notification.getArgs())
                    .text(notification.getType().getAlarmText())
                    .createdAt(notification.getCreatedAt())
                    .updatedAt(notification.getUpdatedAt())
                    .build();
        }
    }
}
