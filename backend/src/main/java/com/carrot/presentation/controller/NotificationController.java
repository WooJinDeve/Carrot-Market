package com.carrot.presentation.controller;

import com.carrot.application.notification.service.NotificationReadService;
import com.carrot.application.notification.service.NotificationWriteService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.carrot.presentation.response.NotificationResponse.NotifyResponses;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationController {

    private final NotificationReadService notificationReadService;
    private final NotificationWriteService notificationWriteService;

    @GetMapping("/notifications")
    public Response<NotifyResponses> notification(@AuthenticationPrincipal LoginUser loginUser,
                                                  Pageable pageable){
        NotifyResponses responses = notificationReadService.getNotifications(loginUser.getId(), pageable);
        return Response.success(responses);
    }

    @GetMapping("/notifications/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal LoginUser loginUser){
        return notificationWriteService.connect(loginUser.getId());
    }
}
