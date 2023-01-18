package com.carrot.application.notification.service;

import com.carrot.application.notification.repository.NotificationRepository;
import com.carrot.testutil.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("[Business] NotificationReadService")
class NotificationReadServiceTest extends ServiceTest {

    @InjectMocks
    private NotificationReadService notificationReadService;

    @Mock
    private NotificationRepository notificationRepository;



}