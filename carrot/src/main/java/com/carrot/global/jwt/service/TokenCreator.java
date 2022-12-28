package com.carrot.global.jwt.service;


import com.carrot.global.jwt.token.AuthToken;

public interface TokenCreator {

    AuthToken createAuthToken(final String userId);

    AuthToken renewAuthToken(final String refreshToken);

    String extractPayload(final String accessToken);
}
