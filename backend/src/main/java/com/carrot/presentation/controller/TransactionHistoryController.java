package com.carrot.presentation.controller;

import com.carrot.application.post.service.PostReadService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.carrot.presentation.response.PostResponse.PostListResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionHistoryController {

    private final PostReadService postReadService;

    @GetMapping("/purchase-history")
    public Response<PostListResponses> getBuyingHistories(@AuthenticationPrincipal LoginUser loginUser){
        PostListResponses responses = postReadService.findByPurchaseHistories(loginUser.getId());
        return Response.success(responses);
    }

    @GetMapping("/sale-history")
    public Response<PostListResponses> getSalesHistories(@AuthenticationPrincipal LoginUser loginUser){
        PostListResponses responses = postReadService.findBySalesHistories(loginUser.getId());
        return Response.success(responses);
    }
}
