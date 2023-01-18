package com.carrot.application.notification.service;

import com.carrot.application.notification.repository.EmitterRepository;
import com.carrot.application.notification.repository.NotificationRepository;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.testutil.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[Business] NotificationWriteService")
class NotificationWriteServiceTest extends ServiceTest {

    @InjectMocks
    private NotificationWriteService notificationWriteService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmitterRepository emitterRepository;



}