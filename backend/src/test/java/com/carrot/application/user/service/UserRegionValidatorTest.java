package com.carrot.application.user.service;

import com.carrot.application.region.domain.Region;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRegionRepository;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.support.ServiceTest;
import com.carrot.support.fixture.UserFixture;
import com.carrot.support.fixture.UserRegionFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.carrot.global.error.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("[Business] UserRegionValidator")
public class UserRegionValidatorTest extends ServiceTest {

    @InjectMocks
    private UserRegionValidator userRegionValidator;
    @Mock
    private UserRegionRepository userRegionRepository;

    @DisplayName("[Error] 사용자의 지역정보가 2개 이상경우 오류를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {2L, 3L, 5L})
    void givenUserId_whenVerify_thenThrowValidNotOverTwoUserRegion(long count) {
        //given
        Long userId = 1L;

        //when
        when(userRegionRepository.countByUserId(userId)).thenReturn(count);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userRegionValidator.validateUserRegionCounter(userId));
        assertThat(USER_REGION_MAX_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 사용자의 지역정보가 1개 이하일 경우 리턴한다")
    @Test
    void givenUserIdAndRegionId_whenVerify_thenPassing() {
        //given
        Long userId = 1L;
        Long count = 1L;

        //when
        when(userRegionRepository.countByUserId(userId)).thenReturn(count);

        //then
        assertThatCode(() -> userRegionValidator.validateUserRegionCounter(userId))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 사용자의 지역정보와 요청한 사용자의 정보가 일치하지 않는 경우")
    @Test
    void givenUser_whenVerify_thenThrowValidNotMatchUser() {
        //given
        User requestUserFixture = UserFixture.get(1L);
        User ownerUserFixture = UserFixture.get(2L);
        UserRegion userRegionFixture = UserRegionFixture.get(1L, ownerUserFixture);

        //when & then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userRegionValidator.validateOwner(requestUserFixture, userRegionFixture));

        assertThat(USER_REGION_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 사용자의 지역정보와 요청한 사용자의 정보가 일치할시, 리턴한다.")
    @Test
    void givenUser_whenVerify_thenPassing() {
        //given
        User userFixture = UserFixture.get(1L);
        UserRegion userRegionFixture = UserRegionFixture.get(1L, userFixture);

        //then
        assertThatCode(() -> userRegionValidator.validateOwner(userFixture, userRegionFixture))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 일치하는 지역이 이미 등록된 경우 에러를 발생시킨다")
    @Test
    void 이미_등록된_지역이_있는경우() {
        //given
        User user = mock(User.class);
        Region region = mock(Region.class);

        //when
        when(userRegionRepository.existsByUserIdAndRegionId(any(), any())).thenReturn(true);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userRegionValidator.validateByUserIdAndRegionId(user, region));
        assertThat(USER_REGION_DUPLICATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 등록된 지역중 일치하는 지역이 없는 경우, 리턴한다.")
    @Test
    void 등록된_지역중_일치하는_지역이_없는_경우() {
        //given
        User user = mock(User.class);
        Region region = mock(Region.class);

        //when
        when(userRegionRepository.existsByUserIdAndRegionId(any(), any())).thenReturn(false);

        //then
        assertThatCode(() -> userRegionValidator.validateByUserIdAndRegionId(user, region))
                .doesNotThrowAnyException();
    }
}
