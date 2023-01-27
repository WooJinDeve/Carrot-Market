package com.carrot.infrastructure.jwt.service;

import com.carrot.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("[Service] TokenService")
class TokenServiceTest extends ServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenCreator tokenCreator;

    @DisplayName("[Success] 토큰 정보 추출 요청")
    @Test
    void givenAccessToken_whenParsing_thenGetUserId(){
        //given
        String accessTokenFixture = "dummyAccessToken";

        //when
        when(tokenCreator.extractPayload(any())).thenReturn("1");

        //then
        Assertions.assertThatCode(() -> tokenService.extractUserId(accessTokenFixture))
                .doesNotThrowAnyException();
    }
}