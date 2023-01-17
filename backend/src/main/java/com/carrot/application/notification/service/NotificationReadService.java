package com.carrot.application.notification.service;

import com.carrot.application.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationReadService {

    private final NotificationRepository notificationRepository;


}
