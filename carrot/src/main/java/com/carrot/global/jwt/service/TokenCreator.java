package com.carrot.global.jwt.service;


import com.carrot.global.jwt.token.AuthToken;

public interface TokenCreator {

    AuthToken createAuthToken(final String email);

    AuthToken renewAuthToken(final String refreshToken);

    String extractPayload(final String accessToken);
}
