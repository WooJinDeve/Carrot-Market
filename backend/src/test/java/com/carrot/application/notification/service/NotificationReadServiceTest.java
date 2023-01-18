package com.carrot.application.notification.service;

import com.carrot.application.notification.domain.Notification;
import com.carrot.application.notification.domain.NotificationArgs;
import com.carrot.application.notification.repository.NotificationRepository;
import com.carrot.application.user.domain.User;
import com.carrot.testutil.ServiceTest;
import com.carrot.testutil.fixture.NotificationFixture;
import com.carrot.testutil.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.carrot.application.notification.domain.NotificationType.CREATE_POST;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("[Business] NotificationReadService")
class NotificationReadServiceTest extends ServiceTest {

    @InjectMocks
    private NotificationReadService notificationReadService;

    @Mock
    private NotificationRepository notificationRepository;

    @DisplayName("[Success] 알림 조회 요청")
    @Test
    void givenUserIdAndPageable_whenFinding_thenFindNotifyResponses() {
        //given
        User userFixture = UserFixture.get(1L);
        Notification notifyFixture = NotificationFixture.get(userFixture, CREATE_POST, new NotificationArgs(1L, 1L));

        PageRequest request = PageRequest.of(0, 20);
        SliceImpl<Notification> fixture = new SliceImpl<>(List.of(notifyFixture), request, false);

        //when
        when(notificationRepository.findAllByReceiverId(any(), any())).thenReturn(fixture);

        //then
        assertThatCode(() -> notificationReadService.getNotifications(userFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 알림 조회 요청시, 조회된 정보가 존재하지 않을 경우")
    @Test
    void givenUserIdAndPageable_whenFinding_thenNotExistAlarm() {
        //given
        User userFixture = UserFixture.get(1L);

        PageRequest request = PageRequest.of(0, 20);

        //when
        when(notificationRepository.findAllByReceiverId(any(), any())).thenReturn(Page.empty());

        //then
        assertThatCode(() -> notificationReadService.getNotifications(userFixture.getId(), request))
                .doesNotThrowAnyException();
    }
}