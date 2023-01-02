package com.carrot.presentation.controller;

import com.carrot.application.post.service.PostReadService;
import com.carrot.application.post.service.PostWriteService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.carrot.presentation.request.PostRequest.PostSaveRequest;
import static com.carrot.presentation.request.PostRequest.PostUpdateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

    @PostMapping
    private Response<Long> create(@AuthenticationPrincipal LoginUser loginUser,
                                  @RequestBody @Valid PostSaveRequest request) {
        Long response = postWriteService.createPost(loginUser.getId(), request);
        return Response.success(response);
    }

    @PutMapping("/{postId}")
    private Response<Void> update(@AuthenticationPrincipal LoginUser loginUser,
                                  @RequestBody @Valid PostUpdateRequest request,
                                  @PathVariable Long postId){
        postWriteService.update(loginUser.getId(), postId, request);
        return Response.success();
    }

    @DeleteMapping("/{postId}")
    private Response<Void> delete(@AuthenticationPrincipal LoginUser loginUser,
                                  @PathVariable Long postId){
        postWriteService.delete(loginUser.getId(), postId);
        return Response.success();
    }
}
