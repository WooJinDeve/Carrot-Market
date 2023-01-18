package com.carrot.testutil.fixture;

import com.carrot.application.notification.domain.NotificationArgs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.carrot.application.notification.domain.NotificationType.NEW_REPLY_ON_COMMENT;
import static com.carrot.presentation.response.NotificationResponse.NotifyResponse;
import static com.carrot.presentation.response.NotificationResponse.NotifyResponses;

public class NotificationFixture {


    public static NotifyResponses getNotifyResponses(long size, boolean hasNext){
        return NotifyResponses.builder()
                .notifications(getConvertToNotifyResponses(size))
                .hasNext(hasNext)
                .build();
    }

    private static List<NotifyResponse> getConvertToNotifyResponses(long size){
        return LongStream.range(1, size + 1)
                .mapToObj(i -> getNotifyResponse(i, i, i))
                .collect(Collectors.toList());
    }

    public static NotifyResponse getNotifyResponse(Long notificationId, Long userId, Long targetId){
        return NotifyResponse.builder()
                .id(notificationId)
                .alarmType(NEW_REPLY_ON_COMMENT)
                .args(new NotificationArgs(userId, targetId))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
