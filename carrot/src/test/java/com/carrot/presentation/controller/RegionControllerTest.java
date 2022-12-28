package com.carrot.presentation.controller;

import com.carrot.application.region.service.RegionService;
import com.carrot.testutil.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegionControllerTest extends ControllerTest {

    @MockBean
    private RegionService regionService;

    @DisplayName("지역명으로 지역정보 검색")
    @Test
    @WithMockUser
    void 지역명으로_지역정보_검색() throws Exception {
        //given
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("state", "none");
        query_param.add("page", "1");

        //when
        when(regionService.search(any(), any())).thenReturn(Page.empty());
        final ResultActions perform = mockMvc.perform(get("/api/v1/locations")
                .params(query_param));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("지역명으로 지역정보 검색 요청시 로그인하지 않은 경우")
    @Test
    @WithAnonymousUser
    void 지역명으로_지역정보_검색시_로그인하지_않은_경우() throws Exception {
        //given
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("state", "none");
        query_param.add("page", "1");

        //when
        when(regionService.search(any(), any())).thenReturn(Page.empty());

        final ResultActions perform = mockMvc.perform(get("/api/v1/locations")
                .params(query_param));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}