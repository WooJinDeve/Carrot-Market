package com.carrot.application.user.service;

import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.support.ServiceTest;
import com.carrot.support.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static com.carrot.global.error.ErrorCode.USER_NOTFOUND_ERROR;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[Business] UserValidator")
public class UserValidatorTest extends ServiceTest {
    @InjectMocks private UserValidator userValidator;

    @DisplayName("[Success] 유저 재가입을 검증한 후, 재가입을 시도한다.")
    @Test
    void 유저_재가입검증() {
        //given
        User fixture = UserFixture.get(1L, now());

        //when & then
        assertThat(fixture.getDeletedAt()).isNotNull();
        assertThatCode(() -> userValidator.validateReRegister(fixture))
                        .doesNotThrowAnyException();
        assertThat(fixture.getDeletedAt()).isNull();
    }

    @DisplayName("[Success] 유저 재가입을 검증한 후, 탈퇴회원이 아닌 경우 로직을 실행하지 않는다.")
    @Test
    void 유저_재가입검증_탈퇴회원아닌_경우_리턴() {
        //given
        User fixture = UserFixture.get(1L);

        //when & then
        assertThat(fixture.getDeletedAt()).isNull();
        assertThatCode(() -> userValidator.validateReRegister(fixture))
                .doesNotThrowAnyException();
        assertThat(fixture.getDeletedAt()).isNull();
    }

    @DisplayName("[Error] 탈퇴 회원을 검증한다")
    @Test
    void 탈퇴_회원을_검증한다() throws Exception {
        //then
        User fixture = UserFixture.get(1L, now());

        //when & then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userValidator.validateDeleted(fixture));
        assertThat(USER_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 탈퇴 회원을 검증시, 탈퇴 회원아 아닌경우 리턴한다.")
    @Test
    void 탈퇴_회원을_검증시_탈퇴회원이_아닌경우() throws Exception {
        //then
        User fixture = UserFixture.get(1L);

        //when & then
        assertThatCode(() -> userValidator.validateDeleted(fixture))
                .doesNotThrowAnyException();
        assertThat(fixture.getDeletedAt()).isNull();
    }
}
