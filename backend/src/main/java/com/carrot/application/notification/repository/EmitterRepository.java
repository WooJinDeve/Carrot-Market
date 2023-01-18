package com.carrot.application.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

public interface EmitterRepository {

    SseEmitter save(Long userId, SseEmitter sseEmitter);

    Optional<SseEmitter> get(Long userId);

    void delete(Long userId);
}
