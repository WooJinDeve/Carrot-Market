package com.carrot.infrastructure.jwt.token;

import com.carrot.global.error.CarrotRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.carrot.global.error.ErrorCode.TOKEN_VALIDATION_ERROR;

@DisplayName("[Token] AuthToken")
class AuthTokenTest {

    @DisplayName("[Success] 리프레시 토큰 검증 요청")
    @Test
    void givenAuthToken_whenVerify_thenPassing() {
        // given
        AuthToken authToken = new AuthToken("dummyAccessToken", "dummyRefreshToken");

        // when & then
        Assertions.assertThatCode(() -> authToken.validateHasSameRefreshToken(authToken.getRefreshToken()))
                        .doesNotThrowAnyException();
    }


    @DisplayName("[Error] 리프레시 토큰 검증시, 일치하지 않는 경우")
    @Test
    void givenAuthToken_whenVerify_thenThrowNotMatchThis() {
        // given
        AuthToken authToken = new AuthToken("dummyAccessToken", "dummyRefreshToken");

        // when & then
        Assertions.assertThatCode(() -> authToken.validateHasSameRefreshToken("invalidRefreshToken"))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(TOKEN_VALIDATION_ERROR.getMessage());
    }
}