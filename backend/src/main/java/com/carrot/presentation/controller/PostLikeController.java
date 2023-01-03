package com.carrot.presentation.controller;

import com.carrot.application.like.service.PostLikeReadService;
import com.carrot.application.like.service.PostLikeWriteService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostLikeController {

    private final PostLikeWriteService postLikeWriteService;
    private final PostLikeReadService postLikeReadService;

    @PostMapping("{postId}/like")
    public Response<Void> like(@AuthenticationPrincipal LoginUser loginUser,
                               @PathVariable Long postId){
        postLikeWriteService.like(loginUser.getId(), postId);
        return Response.success();
    }

    @DeleteMapping("{postId}/like")
    public Response<Void> cancel(@AuthenticationPrincipal LoginUser loginUser,
                               @PathVariable Long postId){
        postLikeWriteService.cancel(loginUser.getId(), postId);
        return Response.success();
    }
}
