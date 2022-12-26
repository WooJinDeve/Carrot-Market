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
    public String save(String email, String refreshToken) {
        TOKEN_REPOSITORY.put(email, refreshToken);
        return TOKEN_REPOSITORY.get(email);
    }

    @Override
    public void deleteAll() {
        TOKEN_REPOSITORY.clear();
    }

    @Override
    public void deleteByMemberId(String email) {
        TOKEN_REPOSITORY.remove(email);
    }

    @Override
    public boolean exist(String email) {
        return TOKEN_REPOSITORY.containsKey(email);
    }

    @Override
    public String getToken(String email) {
        Optional<String> token = Optional.ofNullable(TOKEN_REPOSITORY.get(email));
        return token.orElseThrow(() -> new CarrotRuntimeException(TOKEN_VALIDATION_ERROR));
    }
}
