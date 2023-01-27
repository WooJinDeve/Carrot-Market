package com.carrot.presentation.controller;

import com.carrot.application.post.service.TransactionHistoryReadService;
import com.carrot.config.TestSecurityConfig;
import com.carrot.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponses;
import static com.carrot.support.QueryParamUtil.QueryParam;
import static com.carrot.support.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.support.fixture.TokenFixture.BEARER_TOKEN;
import static com.carrot.support.fixture.TransactionHistoryFixture.getTransactionHistoryListResponses;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TestSecurityConfig.class)
@DisplayName("[View] TransactionHistoryController")
class TransactionHistoryControllerTest extends ControllerTest {

    @MockBean
    private TransactionHistoryReadService transactionHistoryReadService;

    @DisplayName("[GET] 구매 이력 조회 - 요청성공")
    @Test
    @WithMockUser
    void givenUserId_whenFinding_thenPurchasingHistories() throws Exception {
        //given
        Long userId = 1L;
        TransactionHistoryListResponses fixture = getTransactionHistoryListResponses(20L, false);

        //when
        when(transactionHistoryReadService.findByPurchaseHistories(eq(userId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/purchase-history")
                .params(QueryParam())
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 구매 이력 조회시 로그인하지 않은 경우 - 요청실패")
    @Test
    @WithAnonymousUser
    void whenBuyFinding_thenThrowNotLogin() throws Exception {
        //when
        final ResultActions perform = mockMvc.perform(get("/api/v1/purchase-history")
                .params(QueryParam()));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("[GET] 구매 이력 조회 - 요청성공")
    @Test
    @WithMockUser
    void givenUserId_whenFinding_thenSellingHistories() throws Exception {
        //given
        Long userId = 1L;
        TransactionHistoryListResponses fixture = getTransactionHistoryListResponses(20L, false);

        //when
        when(transactionHistoryReadService.findBySalesHistories(eq(userId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/sale-history")
                .params(QueryParam())
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 구매 이력 조회시 로그인하지 않은 경우 - 요청실패")
    @Test
    @WithAnonymousUser
    void whenSalesFinding_thenThrowNotLogin() throws Exception {
        //when
        final ResultActions perform = mockMvc.perform(get("/api/v1/sale-history")
                .params(QueryParam()));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}