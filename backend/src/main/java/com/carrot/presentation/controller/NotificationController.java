package com.carrot.presentation.controller;

import com.carrot.application.notification.service.NotificationReadService;
import com.carrot.application.notification.service.NotificationWriteService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationReadService notificationReadService;
    private final NotificationWriteService notificationWriteService;

    @GetMapping("/notification")
    public Response<?> alarm(@AuthenticationPrincipal LoginUser loginUser,
                             Pageable pageable){

        return null;
    }

    @GetMapping("/notification/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal LoginUser loginUser){
        return notificationWriteService.connect(loginUser.getId());
    }
}
