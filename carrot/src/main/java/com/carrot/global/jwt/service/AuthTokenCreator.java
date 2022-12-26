package com.carrot.global.jwt.service;


import com.carrot.global.jwt.token.AuthToken;
import com.carrot.global.jwt.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokenCreator implements TokenCreator{

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    public AuthToken createAuthToken(final String email) {
        String accessToken = tokenProvider.createAccessToken(email);
        String refreshToken = createRefreshToken(email);
        return new AuthToken(accessToken, refreshToken);
    }

    private String createRefreshToken(final String email) {
        if (tokenRepository.exist(email)) {
            return tokenRepository.getToken(email);
        }
        String refreshToken = tokenProvider.createRefreshToken(email);
        return tokenRepository.save(email, refreshToken);
    }

    @Override
    public AuthToken renewAuthToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        String email = tokenProvider.getPayload(refreshToken);

        String accessTokenForRenew = tokenProvider.createAccessToken(email);
        String refreshTokenForRenew = tokenRepository.getToken(email);

        AuthToken renewalAuthToken = new AuthToken(accessTokenForRenew, refreshTokenForRenew);
        renewalAuthToken.validateHasSameRefreshToken(refreshToken);
        return renewalAuthToken;
    }

    @Override
    public Long extractPayload(String accessToken) {
        tokenProvider.validateToken(accessToken);
        return Long.valueOf(tokenProvider.getPayload(accessToken));
    }
}
