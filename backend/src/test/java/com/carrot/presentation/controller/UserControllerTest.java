package com.carrot.presentation.controller;


import com.carrot.application.user.service.UserWriteService;
import com.carrot.config.TestSecurityConfig;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.testutil.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.carrot.global.error.ErrorCode.*;
import static com.carrot.testutil.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.testutil.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@DisplayName("[View] UserController")
public class UserControllerTest extends ControllerTest {

    @MockBean
    private UserWriteService userWriteService;

    @DisplayName("[View][POST] 사용자 거래 지역정보 저장 - 정상호출")
    @Test
    @WithMockUser
    void 사용자_거래_지역정보_저장() throws Exception {
        //given
        Long regionId = 1L;
        Long userId = 1L;

        //when
        willDoNothing().given(userWriteService).saveRegion(eq(userId), eq(regionId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/users/location/{id}", regionId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[View][POST] 사용자 거래 지역정보 저장 - 예외발생")
    @Test
    @WithMockUser
    void 사용자의_거래_지역이_2개를_초과하는_경우() throws Exception {
        //given
        Long regionId = 1L;
        Long userId = 1L;

        //when
        doThrow(new CarrotRuntimeException(USER_REGION_MAX_ERROR)).when(userWriteService).saveRegion(eq(userId), eq(regionId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/users/location/{id}", regionId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("[View][POST] 사용자 거래 지역정보 저장 - 예외발생")
    @Test
    @WithMockUser
    void 동일지역_저장을_요청한_경우() throws Exception {
        //given
        Long regionId = 1L;
        Long userId = 1L;
        //when
        doThrow(new CarrotRuntimeException(USER_REGION_DUPLICATION_ERROR)).when(userWriteService).saveRegion(eq(userId), eq(regionId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/users/location/{id}", regionId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("[View][DELETE] - 사용자 거래 지역정보 삭제 - 요청성공")
    @Test
    @WithMockUser
    void 사용자_거래_지역정보_삭제() throws Exception {
        //given
        Long userRegionId = 1L;
        Long userId = 1L;

        //when
        willDoNothing().given(userWriteService).deleteRegion(eq(userId), eq(userRegionId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/users/location/{id}", userRegionId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[View][DELETE] - 사용자 거래 지역정보 삭제 - 예외발생")
    @Test
    @WithMockUser
    void 해당_지역정보가_존재하지_않을_경우() throws Exception {
        //given
        Long userRegionId = 1L;
        Long userId = 1L;

        //when
        doThrow(new CarrotRuntimeException(USER_REGION_NOTFOUND_ERROR)).when(userWriteService).deleteRegion(eq(userId), eq(userRegionId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/users/location/{id}", userRegionId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("[View][DELETE] - 사용자 거래 지역정보 삭제 - 예외발생")
    @Test
    @WithMockUser
    void 지역정보_삭제시_타인이_삭제요청할_경우() throws Exception {
        //given
        Long userRegionId = 1L;

        //when
        doThrow(new CarrotRuntimeException(USER_REGION_VALIDATION_ERROR)).when(userWriteService).deleteRegion(any(), eq(userRegionId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/users/location/{id}", userRegionId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
