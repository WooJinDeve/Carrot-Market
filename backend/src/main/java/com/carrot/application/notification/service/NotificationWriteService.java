package com.carrot.application.notification.service;

import com.carrot.application.notification.domain.Notification;
import com.carrot.application.notification.domain.NotificationArgs;
import com.carrot.application.notification.domain.NotificationType;
import com.carrot.application.notification.repository.EmitterRepository;
import com.carrot.application.notification.repository.NotificationRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static com.carrot.global.error.ErrorCode.NOTIFICATION_CONNECT_ERROR;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationWriteService {

    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String NOTIFICATION_NAME = "carrot";

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;

    public void send(Long userId, NotificationType type, NotificationArgs args) {
        User receiver = userRepository.getById(userId);
        Notification notification = notificationRepository.save(Notification.of(receiver, type, args));

        emitterRepository.get(userId)
                .ifPresentOrElse(sseEmitter -> sendToClient(sseEmitter, notification.getId(), userId, notification.getType().name()),
                        () -> log.info("[INFO] No emitter founded : {}", userId));
    }

    private void sendToClient(SseEmitter sseEmitter, Long notifyId, Long userId, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(String.valueOf(notifyId))
                    .name(NOTIFICATION_NAME)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.delete(userId);
            throw new CarrotRuntimeException(NOTIFICATION_CONNECT_ERROR);
        }
    }

    @SneakyThrows
    public SseEmitter connect(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);
        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));
        sseEmitter.send(SseEmitter.event().id("").name(NOTIFICATION_NAME).data("connect completed"));
        return sseEmitter;
    }
}
