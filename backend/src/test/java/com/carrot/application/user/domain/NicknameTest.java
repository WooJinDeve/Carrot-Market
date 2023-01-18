package com.carrot.application.user.domain;

import com.carrot.global.error.CarrotRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.carrot.global.error.ErrorCode.NICKNAME_LENGTH_VALIDATION_ERROR;

@DisplayName("[Domain] Nickname")
class NicknameTest {

    @DisplayName("[Error] 닉네임 길이가가 최대범위를 넘어가면 오류가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"aaaaaaaaaaaaaaaa"})
    void givenNickname_whenConstruct_thenThrowValidateNickname(final String nickname) {
        Assertions.assertThatCode(() -> new Nickname(nickname))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(NICKNAME_LENGTH_VALIDATION_ERROR.getMessage());
    }

    @DisplayName("[Success] 닉네임 생성 요청")
    @ParameterizedTest
    @ValueSource(strings = {"a", "aaaaaaaaaaaaaaa"})
    void givenNickname_whenConstruct_thenConstructedNickname(final String nickname) {
        Assertions.assertThatCode(() -> new Nickname(nickname))
                .doesNotThrowAnyException();
    }

}