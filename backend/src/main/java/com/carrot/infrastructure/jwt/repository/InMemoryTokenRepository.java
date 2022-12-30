package com.carrot.global.jwt.repository;

import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.carrot.global.error.ErrorCode.TOKEN_VALIDATION_ERROR;

@Component
public class InMemoryTokenRepository implements TokenRepository{

    private static final Map<String, String> TOKEN_REPOSITORY = new ConcurrentHashMap<>();

    @Override
    public String save(String id, String refreshToken) {
        TOKEN_REPOSITORY.put(id, refreshToken);
        return TOKEN_REPOSITORY.get(id);
    }

    @Override
    public void deleteAll() {
        TOKEN_REPOSITORY.clear();
    }

    @Override
    public void deleteByMemberId(String id) {
        TOKEN_REPOSITORY.remove(id);
    }

    @Override
    public boolean exist(String id) {
        return TOKEN_REPOSITORY.containsKey(id);
    }

    @Override
    public String getToken(String id) {
        Optional<String> token = Optional.ofNullable(TOKEN_REPOSITORY.get(id));
        return token.orElseThrow(() -> new CarrotRuntimeException(TOKEN_VALIDATION_ERROR));
    }
}
