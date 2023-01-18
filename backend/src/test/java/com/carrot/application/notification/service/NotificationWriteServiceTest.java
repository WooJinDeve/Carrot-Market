package com.carrot.application.notification.service;

import com.carrot.application.notification.domain.Notification;
import com.carrot.application.notification.domain.NotificationArgs;
import com.carrot.application.notification.domain.NotificationType;
import com.carrot.application.notification.repository.EmitterRepository;
import com.carrot.application.notification.repository.NotificationRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.testutil.ServiceTest;
import com.carrot.testutil.fixture.NotificationFixture;
import com.carrot.testutil.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

import static com.carrot.application.notification.domain.NotificationType.NEW_COMMENT_ON_POST;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @DisplayName("[Success] 알림 저장 및 전송 요청")
    @Test
    void givenNotification_whenSending_thenSendAlarm() {
        //given
        User receiverFixture = UserFixture.get(1L);
        NotificationType type = NEW_COMMENT_ON_POST;
        NotificationArgs args = new NotificationArgs(2L, 1L);
        Notification notifyFixture = NotificationFixture.get(receiverFixture, type, args);
        SseEmitter sseEmitterFixture = mock(SseEmitter.class);

        //when
        when(userRepository.getById(any())).thenReturn(receiverFixture);
        when(notificationRepository.save(any())).thenReturn(notifyFixture);
        when(emitterRepository.get(receiverFixture.getId())).thenReturn(Optional.ofNullable(sseEmitterFixture));

        //then
        assertThatCode(() -> notificationWriteService.send(receiverFixture.getId(), type, args))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 알림 저장 및 전송시, Sse 정보가 저장되지 않았을 경우")
    @Test
    void givenNotification_whenSending_thenNotExistSseEmitter() {
        //given
        User receiverFixture = UserFixture.get(1L);
        NotificationType type = NEW_COMMENT_ON_POST;
        NotificationArgs args = new NotificationArgs(2L, 1L);
        Notification notifyFixture = NotificationFixture.get(receiverFixture, type, args);

        //when
        when(userRepository.getById(any())).thenReturn(receiverFixture);
        when(notificationRepository.save(any())).thenReturn(notifyFixture);
        when(emitterRepository.get(receiverFixture.getId())).thenReturn(Optional.empty());

        //then
        assertThatCode(() -> notificationWriteService.send(receiverFixture.getId(), type, args))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] SSE 연결 요청")
    @Test
    void givenUserId_whenConnecting_thenConnectedUser() {
        //given
        Long userId = 1L;

        //when
        when(emitterRepository.save(eq(userId), any())).thenReturn(new SseEmitter(60L * 1000 * 60));

        //then
        assertThatCode(() -> notificationWriteService.connect(userId))
                .doesNotThrowAnyException();
    }
}