package com.carrot.infrastructure.jwt.service;


import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.infrastructure.jwt.repository.TokenRepository;
import com.carrot.infrastructure.jwt.token.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.carrot.global.error.ErrorCode.TOKEN_NOTFOUND_ERROR;

@Component
@RequiredArgsConstructor
public class AuthTokenCreator implements TokenCreator{

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    public AuthToken createAuthToken(final String userId) {
        String accessToken = tokenProvider.createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);
        return new AuthToken(accessToken, refreshToken);
    }

    private String createRefreshToken(final String userId) {
        if (tokenRepository.exist(userId)) {
            return tokenRepository.getToken(userId);
        }
        String refreshToken = tokenProvider.createRefreshToken(userId);
        return tokenRepository.save(userId, refreshToken);
    }

    @Override
    public AuthToken renewAuthToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        String userId = tokenProvider.getPayload(refreshToken);

        String accessTokenForRenew = tokenProvider.createAccessToken(userId);
        String refreshTokenForRenew = tokenRepository.getToken(userId);

        AuthToken renewalAuthToken = new AuthToken(accessTokenForRenew, refreshTokenForRenew);
        renewalAuthToken.validateHasSameRefreshToken(refreshToken);
        return renewalAuthToken;
    }

    @Override
    public String extractPayload(String accessToken) {
        tokenProvider.validateToken(accessToken);
        String id = tokenProvider.getPayload(accessToken);
        validateExistToken(id);
        return id;
    }

    private void validateExistToken(String id){
        if (!tokenRepository.exist(id)){
            throw new CarrotRuntimeException(TOKEN_NOTFOUND_ERROR);
        }
    }
}
