package com.carrot.application.notification.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisEmitterRepository implements EmitterRepository{

    private final RedisTemplate<String, SseEmitter> redisEmitterTemplate;
    private final static Duration EMITTER_TTL = Duration.ofMinutes(3);

    @Override
    public SseEmitter save(Long userId, SseEmitter sseEmitter){
        final String key = getKey(userId);
        redisEmitterTemplate.opsForValue().set(key, sseEmitter, EMITTER_TTL);
        log.info("[INFO] SET sseEmitter {} : {}", key, sseEmitter);
        return sseEmitter;
    }

    @Override
    public Optional<SseEmitter> get(Long userId){
        final String key = getKey(userId);
        log.info("[INFO] GET sseEmitter {}", key);
        return Optional.ofNullable(redisEmitterTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(Long userId){
        String key = getKey(userId);
        log.info("[INFO] DELETE sseEmitter {} : {}", userId, key);
        redisEmitterTemplate.delete(key);
    }

    private String getKey(Long userId){
        return "Emitter:UID:" + userId;
    }

}
