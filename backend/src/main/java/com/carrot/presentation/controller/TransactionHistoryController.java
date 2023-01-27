package com.carrot.presentation.controller;

import com.carrot.application.post.service.TransactionHistoryReadService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.carrot.presentation.response.TransactionHistoryResponse.TransactionHistoryListResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionHistoryController {

    private final TransactionHistoryReadService transactionHistoryReadService;

    @GetMapping("/purchase-history")
    public Response<TransactionHistoryListResponses> getBuyingHistories(@AuthenticationPrincipal LoginUser loginUser,
                                                                        final Pageable pageable){
        TransactionHistoryListResponses responses = transactionHistoryReadService.findByPurchaseHistories(loginUser.getId(), pageable);
        return Response.success(responses);
    }

    @GetMapping("/sale-history")
    public Response<TransactionHistoryListResponses> getSalesHistories(@AuthenticationPrincipal LoginUser loginUser,
                                                                       final Pageable pageable){
        TransactionHistoryListResponses responses = transactionHistoryReadService.findBySalesHistories(loginUser.getId(), pageable);
        return Response.success(responses);
    }
}
