package com.carrot.infrastructure.jwt.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository{

    private final RedisTemplate<String, String> redisTemplate;
    private final static Duration TOKEN_CACHE_TTL = Duration.ofDays(14);


    @Override
    public String save(String id, String refreshToken) {
        String key = getKey(id);
        log.info("[INFO] TOKEN INSERT USER ID : {}, Redis KEY : {}",id, key);
        redisTemplate.opsForValue().set(key, refreshToken, TOKEN_CACHE_TTL);
        return refreshToken;
    }

    @Override
    public void deleteByMemberId(String id) {
        String key = getKey(id);
        log.info("[INFO] TOKEN DELETE USER ID : {}, Redis KEY : {}",id, key);
        redisTemplate.delete(key);
    }

    @Override
    public boolean exist(String id) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getKey(id)));
    }

    @Override
    public String getToken(String id) {
        String key = getKey(id);
        log.info("[INFO] TOKEN GET USER ID : {}, Redis KEY : {}",id, key);
        return  redisTemplate.opsForValue().get(getKey(key));
    }

    private String getKey(String id){
        return "TOKEN:" + id;
    }
}
