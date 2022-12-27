package com.carrot.presentation.controller;

import com.carrot.application.region.service.RegionService;
import com.carrot.testutil.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegionControllerTest extends ControllerTest {
    @Autowired
    private RegionService regionService;

    @DisplayName("지역명으로 지역정보 검색")
    @Test
    @WithMockUser
    void 지역명으로_지역정보_검색() throws Exception {
        when(regionService.search(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/locations")
                .param("state", anyString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("지역명으로 지역정보 검색 요청시 로그인하지 않은 경우")
    @Test
    @WithAnonymousUser
    void 지역명으로_지역정보_검색시_로그인하지_않은_경우() throws Exception {
        when(regionService.search(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/locations")
                        .param("state", anyString()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}