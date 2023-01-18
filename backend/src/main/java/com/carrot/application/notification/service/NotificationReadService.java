package com.carrot.application.notification.service;

import com.carrot.application.notification.domain.Notification;
import com.carrot.application.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.presentation.response.NotificationResponse.NotifyResponse;
import static com.carrot.presentation.response.NotificationResponse.NotifyResponses;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationReadService {

    private final NotificationRepository notificationRepository;

    public NotifyResponses getNotifications(Long userId, Pageable pageable) {
        Slice<Notification> response = notificationRepository.findAllByReceiverId(userId, pageable);
        return NotifyResponses.builder()
                .notifications(convertToNotifyResponse(response.getContent()))
                .hasNext(response.hasNext())
                .build();
    }

    private List<NotifyResponse> convertToNotifyResponse(List<Notification> notifications){
        return notifications.stream()
                .map(NotifyResponse::of)
                .collect(Collectors.toList());
    }
}
