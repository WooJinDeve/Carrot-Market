package com.carrot.infrastructure.jwt.token;

import com.carrot.global.error.CarrotRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.carrot.global.error.ErrorCode.TOKEN_VALIDATION_ERROR;

@Getter
@NoArgsConstructor
public class AuthToken {

    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthToken(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void validateHasSameRefreshToken(final String otherRefreshToken) {
        if (!refreshToken.equals(otherRefreshToken)) {
            throw new CarrotRuntimeException(TOKEN_VALIDATION_ERROR);
        }
    }
}
