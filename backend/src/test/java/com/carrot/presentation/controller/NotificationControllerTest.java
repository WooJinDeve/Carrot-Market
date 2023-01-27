package com.carrot.presentation.controller;

import com.carrot.application.notification.service.NotificationReadService;
import com.carrot.application.notification.service.NotificationWriteService;
import com.carrot.config.TestSecurityConfig;
import com.carrot.support.ControllerTest;
import com.carrot.support.fixture.NotificationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.carrot.presentation.response.NotificationResponse.NotifyResponses;
import static com.carrot.support.QueryParamUtil.QueryParam;
import static com.carrot.support.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.support.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@DisplayName("[View] NotificationController")
public class NotificationControllerTest extends ControllerTest {


    @MockBean
    private NotificationWriteService notificationWriteService;

    @MockBean
    private NotificationReadService notificationReadService;

    @DisplayName("[GET] 알림 페이징 조회 - 요청성공")
    @Test
    @WithMockUser
    void givenUserId_whenFinding_thenFindNotifyResponses() throws Exception {
        //given
        Long userId = 1L;

        NotifyResponses fixture = NotificationFixture.getNotifyResponses(20, true);

        //when
        when(notificationReadService.getNotifications(eq(userId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/notifications")
                .params(QueryParam())
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 알림 페이징 조회시, 로그인하지 않은 경우 - 요청실패")
    @Test
    @WithAnonymousUser
    void whenFinding_thenThrowNotLogin() throws Exception {
        //when
        final ResultActions perform = mockMvc.perform(get("/api/v1/notifications")
                .params(QueryParam()));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[GET] 서버 사이드 이벤트 알림 구독 요청 - 요청 성공")
    @Test
    void givenUserId_whenSubscribing_thenSubscribe() throws Exception {
        //given
        Long userId = 1L;

        //when
        when(notificationWriteService.connect(userId)).thenReturn(mock(SseEmitter.class));

        final ResultActions perform = mockMvc.perform(get("/api/v1/notifications/subscribe")
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 서버 사이드 이벤트 알림 구독 요청시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void given_whenSubscribing_thenThrowNotLogin() throws Exception {
        //given & when
        final ResultActions perform = mockMvc.perform(get("/api/v1/notifications/subscribe"));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
