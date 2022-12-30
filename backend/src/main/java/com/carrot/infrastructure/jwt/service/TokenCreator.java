package com.carrot.infrastructure.jwt.service;


import com.carrot.infrastructure.jwt.token.AuthToken;

public interface TokenCreator {

    AuthToken createAuthToken(final String userId);

    AuthToken renewAuthToken(final String refreshToken);

    String extractPayload(final String accessToken);
}
