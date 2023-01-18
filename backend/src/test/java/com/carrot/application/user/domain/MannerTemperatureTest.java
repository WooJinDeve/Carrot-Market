package com.carrot.application.user.domain;

import com.carrot.global.error.CarrotRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.carrot.global.error.ErrorCode.MANNERTEMPERATURE_OVERFLOW_ERROR;

@DisplayName("[Domain] MannerTemperature")
class MannerTemperatureTest {

    @DisplayName("[Error] 매너온도가 최소/최대범위를 넘어가면 오류가 발생한다.")
    @ParameterizedTest
    @ValueSource(doubles = {99.1, -1.1, -0.1, 100.1})
    void givenMannerTemperature_whenConstruct_thenThrowValidateMannerTemperature(final Double mannerTemperature) {
        Assertions.assertThatCode(() -> new MannerTemperature(mannerTemperature))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(MANNERTEMPERATURE_OVERFLOW_ERROR.getMessage());
    }

    @DisplayName("[Success] 매너온도 생성 요청")
    @ParameterizedTest
    @ValueSource(doubles = {99, 0, 1, 98})
    void givenMannerTemperature_whenConstruct_thenConstructedMannerTemperature(final Double mannerTemperature) {
        Assertions.assertThatCode(() -> new MannerTemperature(mannerTemperature))
                .doesNotThrowAnyException();
    }

}