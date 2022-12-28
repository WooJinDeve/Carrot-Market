package com.carrot.presentation.controller;


import com.carrot.application.user.service.UserReadService;
import com.carrot.application.user.service.UserWriteService;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.presentation.request.UserRegionRequest;
import com.carrot.testutil.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.carrot.global.error.ErrorCode.USER_REGION_MAX_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTest {

    @MockBean
    private UserWriteService userWriteService;

    @MockBean
    private UserReadService userReadService;

    @DisplayName("지역정보 저장 요청 성공")
    @Test
    @WithMockUser
    void 지역정보_저장_요청_성공() throws Exception {
        //given
        Long regionId = 1L;

        //when
        doNothing().when(userWriteService).saveUserRegion(any(), eq(regionId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/users/location")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserRegionRequest(regionId))));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("지역정보 저장 요청시 로그인하지 않은 경우")
    @Test
    @WithAnonymousUser
    void 지역정보_저장_요청시_로그인하지_않은_경우() throws Exception {
        //given
        Long regionId = 1L;

        //when
        doNothing().when(userWriteService).saveUserRegion(any(), eq(regionId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/users/location")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserRegionRequest(regionId))));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("사용자의 지역정보가 2개 지역을 초과하는 경우 예외 발생")
    @Test
    @WithAnonymousUser
    void 사용자의_지역정보가_2개_지역을_초과하는_경우_예외_발생() throws Exception {
        //given
        Long regionId = 1L;

        //when
        doThrow(new CarrotRuntimeException(USER_REGION_MAX_ERROR)).when(userWriteService).saveUserRegion(any(), eq(regionId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/users/location")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserRegionRequest(regionId))));

        //then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

}
