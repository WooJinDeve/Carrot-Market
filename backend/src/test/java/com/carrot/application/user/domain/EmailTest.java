package com.carrot.application.user.domain;

import com.carrot.global.error.CarrotRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.carrot.global.error.ErrorCode.EMAIL_VALIDATION_ERROR;

@DisplayName("[Domain] Email")
class EmailTest {

    @DisplayName("[Error] 이메일 형식과 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"default@", "default.email.com", "default.email","default@email", "@email.com"})
    void givenEmail_whenConstruct_thenThrowValidateEmail(final String email) {
        Assertions.assertThatCode(() -> new Email(email))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(EMAIL_VALIDATION_ERROR.getMessage());
    }


    @DisplayName("[Success] 이메일 생성 요청")
    @ParameterizedTest
    @ValueSource(strings = {"default@naver.com", "d12@email.com", "default@email.com"})
    void givenEmail_whenConstruct_thenConstructedEmail(final String email) {
        Assertions.assertThatCode(() -> new Email(email))
                .doesNotThrowAnyException();
    }
}