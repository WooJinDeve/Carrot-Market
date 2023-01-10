package com.carrot.infrastructure.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenCreator tokenCreator;
    public Long extractUserId(final String accessToken) {
        return Long.parseLong(tokenCreator.extractPayload(accessToken));
    }
}
