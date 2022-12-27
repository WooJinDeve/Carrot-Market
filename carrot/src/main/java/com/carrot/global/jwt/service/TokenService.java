package com.carrot.global.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenCreator tokenCreator;

    public String extractMemberEmail(final String accessToken) {
        return tokenCreator.extractPayload(accessToken);
    }
}
