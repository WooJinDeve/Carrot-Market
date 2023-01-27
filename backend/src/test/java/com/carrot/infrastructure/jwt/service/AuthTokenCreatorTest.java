package com.carrot.infrastructure.jwt.service;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.infrastructure.jwt.repository.TokenRepository;
import com.carrot.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.carrot.global.error.ErrorCode.TOKEN_NOTFOUND_ERROR;
import static com.carrot.global.error.ErrorCode.TOKEN_VALIDATION_ERROR;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DisplayName("[Service] AuthTokenCreator")
class AuthTokenCreatorTest extends ServiceTest {

    @InjectMocks
    private AuthTokenCreator authTokenCreator;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @DisplayName("[Success] 토큰 생성 요청")
    @Test
    void givenUserId_whenCreating_thenCreatedToken() {
        //given
        String userId = "1";
        String accessTokenFixture = "dummyAccessToken";
        String refreshTokenFixture = "dummyRefreshToken";

        //when
        when(tokenProvider.createAccessToken(any())).thenReturn(accessTokenFixture);
        when(tokenRepository.exist(any())).thenReturn(false);
        when(tokenProvider.createRefreshToken(userId)).thenReturn(refreshTokenFixture);
        when(tokenRepository.save(any(), any())).thenReturn(refreshTokenFixture);

        //then
        assertThatCode(() -> authTokenCreator.createAuthToken(userId))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 토큰 생성 요청시, 이미 저장된 토큰이 있는 경우")
    @Test
    void givenUserId_whenCreating_thenExistToken() {
        //given
        String userId = "1";
        String accessTokenFixture = "dummyAccessToken";
        String refreshTokenFixture = "dummyRefreshToken";

        //when
        when(tokenProvider.createAccessToken(any())).thenReturn(accessTokenFixture);
        when(tokenRepository.exist(any())).thenReturn(true);
        when(tokenRepository.getToken(any())).thenReturn(refreshTokenFixture);

        //then
        assertThatCode(() -> authTokenCreator.createAuthToken(userId))
                .doesNotThrowAnyException();
    }


    @DisplayName("[Success] 토큰 재발급 요청")
    @Test
    void givenRefreshToken_whenVerify_thenRenewToken(){
        //given
        String userIdFixture = "1";
        String accessTokenFixture = "dummyAccessToken";
        String refreshTokenFixture = "dummyRefreshToken";


        //when
        doNothing().when(tokenProvider).validateToken(refreshTokenFixture);
        when(tokenProvider.getPayload(any())).thenReturn(userIdFixture);
        when(tokenProvider.createAccessToken(any())).thenReturn(accessTokenFixture);
        when(tokenRepository.getToken(any())).thenReturn(refreshTokenFixture);


        //then
        assertThatCode(() -> authTokenCreator.renewAuthToken(refreshTokenFixture))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 토큰 재발급 요청시, 리프레쉬 토큰이 다른 경우 에러 발생")
    @Test
    void givenRefreshToken_whenVerify_thenThrowValidNotMatchRefreshToken(){
        //given
        String userIdFixture = "1";
        String accessTokenFixture = "dummyAccessToken";
        String refreshTokenFixture = "dummyRefreshToken";
        String notMatchRefreshTokenFixture = "dummyNotMatchRefreshToken";

        //when
        doNothing().when(tokenProvider).validateToken(refreshTokenFixture);
        when(tokenProvider.getPayload(any())).thenReturn(userIdFixture);
        when(tokenProvider.createAccessToken(any())).thenReturn(accessTokenFixture);
        when(tokenRepository.getToken(any())).thenReturn(notMatchRefreshTokenFixture);

        //then
        assertThatCode(() -> authTokenCreator.renewAuthToken(refreshTokenFixture))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(TOKEN_VALIDATION_ERROR.getMessage());
    }

    @DisplayName("[Success] 토큰 검증 요청")
    @Test
    void givenAccessToken_whenVerify_thenPassing(){
        //given
        String userIdFixture = "1";
        String accessTokenFixture = "dummyAccessToken";

        //when
        doNothing().when(tokenProvider).validateToken(accessTokenFixture);
        when(tokenProvider.getPayload(any())).thenReturn(userIdFixture);
        when(tokenRepository.exist(any())).thenReturn(true);

        //then
        assertThatCode(() -> authTokenCreator.extractPayload(accessTokenFixture))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 토큰 검증 요청시, 리프레시 토큰이 저장되어 있지 않은 경우")
    @Test
    void givenAccessToken_whenVerify_thenThrowValidNotExistRefreshToken(){
        //given
        String userIdFixture = "1";
        String accessTokenFixture = "dummyAccessToken";

        //when
        doNothing().when(tokenProvider).validateToken(accessTokenFixture);
        when(tokenProvider.getPayload(any())).thenReturn(userIdFixture);
        when(tokenRepository.exist(any())).thenReturn(false);

        //then
        assertThatCode(() -> authTokenCreator.extractPayload(accessTokenFixture))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(TOKEN_NOTFOUND_ERROR.getMessage());
    }
}