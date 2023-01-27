package com.carrot.application.user.service;

import com.carrot.application.region.domain.Region;
import com.carrot.application.region.repository.RegionRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRegionRepository;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.infrastructure.oauth2.provider.ProviderUser;
import com.carrot.support.ServiceTest;
import com.carrot.support.fixture.RegionFixture;
import com.carrot.support.fixture.UserFixture;
import com.carrot.support.fixture.UserRegionFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("[Business] UserWriteService")
class UserWriteServiceTest extends ServiceTest {

    @InjectMocks private UserWriteService userWriteService;

    @Mock private UserRepository userRepository;
    @Mock private RegionRepository regionRepository;
    @Mock private UserRegionRepository userRegionRepository;
    @Mock private UserValidator userValidator;
    @Mock private UserRegionValidator userRegionValidator;


    @DisplayName("[Sucess] OAuth2 회원가입을 진행하고, 회원의 ID를 반환한다.")
    @Test
    void givenUserId_whenSaving_thenRegisterOAuth2User(){
        //given
        ProviderUser mock = mock(ProviderUser.class);
        Long id = 1L;

        //when
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(UserFixture.get(id));

        //then
        assertThatCode(() -> userWriteService.register(mock))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Sucess] 유저 지역정보를 저장한다")
    @Test
    void givenUserIdAndRegionId_whenSaving_thenSaveUserRegionInformation(){
        //given
        Long userid = 1L;
        Long regionId = 1L;

        User userFixture = UserFixture.get(userid);
        Region regionFixture = RegionFixture.get(regionId);

        //when
        when(userRepository.getById(userid)).thenReturn(userFixture);
        when(regionRepository.getById(regionId)).thenReturn(regionFixture);
        when(userRegionRepository.save(any())).thenReturn(mock(UserRegion.class));

        //then
        assertThatCode(() -> userWriteService.saveRegion(userid, regionId))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 유저 지역정보 저장시, 탈퇴 회원을 검증한다.")
    @Test
    void givenUserIdAndRegionId_whenVerifying_thenThrowValidDeletedUser() {
        //given
        Long userid = 1L;
        Long regionId = 1L;

        User fixture = UserFixture.get(userid, now());

        //when
        when(userRepository.getById(userid)).thenReturn(fixture);
        doThrow(new CarrotRuntimeException(USER_NOTFOUND_ERROR)).when(userValidator)
                        .validateDeleted(fixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userWriteService.saveRegion(userid, regionId));
        assertThat(USER_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 유저 지역정보 저장시, 지역이 없는 경우")
    @Test
    void givenUserIdAndRegionId_whenVerify_thenThrowValidNotExistUserRegion() {
        //given
        Long userid = 1L;
        Long regionId = 1L;

        User fixture = UserFixture.get(userid, now());

        //when
        when(userRepository.getById(userid)).thenReturn(fixture);
        doThrow(new CarrotRuntimeException(REGION_NOTFOUND_ERROR)).when(regionRepository)
                .getById(regionId);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userWriteService.saveRegion(userid, regionId));
        assertThat(REGION_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 유저 지역정보 저장시, 이미 해당 지역이 저장된 경우")
    @Test
    void givenUserIdAndRegionId_whenVerify_thenThrowValidDuplicatedRegion() {
        //given
        Long userid = 1L;
        Long regionId = 1L;

        User userFixture = UserFixture.get(userid);
        Region regionFixture = RegionFixture.get(regionId);

        //when
        when(userRepository.getById(userid)).thenReturn(userFixture);
        when(regionRepository.getById(regionId)).thenReturn(regionFixture);
        doThrow(new CarrotRuntimeException(USER_REGION_DUPLICATION_ERROR))
                .when(userRegionValidator).validateByUserIdAndRegionId(userFixture, regionFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userWriteService.saveRegion(userid, regionId));
        assertThat(USER_REGION_DUPLICATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 유저 지역정보 저장시, 사용자의 지역 정보가 2개 이상 저장된 경우")
    @Test
    void givenUserIdAndRegionId_whenVerify_thenThrowValidNotOverTwoUserRegion() {
        //given
        Long userid = 1L;
        Long regionId = 1L;

        User userFixture = UserFixture.get(userid);
        Region regionFixture = RegionFixture.get(regionId);

        //when
        when(userRepository.getById(userid)).thenReturn(userFixture);
        when(regionRepository.getById(regionId)).thenReturn(regionFixture);
        doThrow(new CarrotRuntimeException(USER_REGION_MAX_ERROR))
                .when(userRegionValidator).validateUserRegionCounter(userid);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userWriteService.saveRegion(userid, regionId));
        assertThat(USER_REGION_MAX_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 사용자 지역 삭제 요청")
    @Test
    void givenUserIdAndUserRegionId_whenDeleting_thenDeletedUser() {
        //given
        Long userid = 1L;
        Long userRegionId = 1L;

        User userFixture = UserFixture.get(userid);
        UserRegion userRegionFixture = UserRegionFixture.get(userRegionId, userFixture);

        //when
        when(userRepository.getById(userid)).thenReturn(userFixture);
        when(userRegionRepository.getById(userid)).thenReturn(userRegionFixture);
        doNothing().when(userRegionRepository).delete(userRegionFixture);

        //then
        assertThatCode(() -> userWriteService.deleteRegion(userid, userRegionId))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 사용자 지역 삭제 요청시, 탈퇴 회원을 검증한다.")
    @Test
    void givenUserIdAndUserRegionId_whenDeleting_thenThrowValidSoftDeletedUser() {
        //given
        Long userid = 1L;
        Long userRegionId = 1L;

        User fixture = UserFixture.get(userid);

        //when
        when(userRepository.getById(userid)).thenReturn(fixture);
        doThrow(new CarrotRuntimeException(USER_NOTFOUND_ERROR)).when(userValidator)
                .validateDeleted(fixture);
        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userWriteService.deleteRegion(userid, userRegionId));
        assertThat(USER_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 사용자 지역 삭제 요청시, 저장된 지역정보가 없는 경우")
    @Test
    void givenUserIdAndUserRegionId_whenDeleting_thenThrowValidNotExistUserRegionInformation() {
        //given
        Long userid = 1L;
        Long userRegionId = 1L;

        User fixture = UserFixture.get(userid);

        //when
        when(userRepository.getById(userid)).thenReturn(fixture);
        doThrow(new CarrotRuntimeException(USER_REGION_NOTFOUND_ERROR)).when(userRegionRepository)
                .getById(userRegionId);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userWriteService.deleteRegion(userid, userRegionId));
        assertThat(USER_REGION_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }


    @DisplayName("[Error] 사용자 지역 삭제 요청시, 해당 사용자와 지역정보의 회원이 일치하지 않은 경우")
    @Test
    void givenUserIdAndUserRegionId_whenDeleting_thenThrowValidNotMatchRegionInformation() {
        //given
        Long userid = 1L;
        Long userRegionId = 1L;

        User userFixture = UserFixture.get(userid);
        UserRegion userRegionFixture = UserRegionFixture.get(userRegionId, userFixture);

        //when
        when(userRepository.getById(userid)).thenReturn(userFixture);
        when(userRegionRepository.getById(userid)).thenReturn(userRegionFixture);
        doThrow(new CarrotRuntimeException(USER_REGION_VALIDATION_ERROR)).when(userRegionValidator)
                .validateOwner(userFixture, userRegionFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> userWriteService.deleteRegion(userid, userRegionId));
        assertThat(USER_REGION_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }
}