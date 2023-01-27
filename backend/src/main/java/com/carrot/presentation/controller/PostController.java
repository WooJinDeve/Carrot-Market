package com.carrot.presentation.controller;

import com.carrot.application.post.service.PostReadService;
import com.carrot.application.post.service.PostWriteService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.carrot.presentation.request.PostRequest.*;
import static com.carrot.presentation.response.PostResponse.PostDetailResponse;
import static com.carrot.presentation.response.PostResponse.PostListResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

    @GetMapping
    public Response<PostListResponses> getSearch(@AuthenticationPrincipal LoginUser loginUser,
                                                 @RequestBody PostSearchRequest request,
                                                 final Pageable pageable) {
        PostListResponses responses = postReadService.findBySearchConditions(
                loginUser.getId(),
                request.getCategory(),
                request.getTitle(),
                pageable);
        return Response.success(responses);
    }

    @GetMapping("/{postId}")
    public Response<PostDetailResponse> getById(@AuthenticationPrincipal LoginUser loginUser,
                                                @PathVariable Long postId) {
        PostDetailResponse response = postReadService.findById(postId);
        return Response.success(response);
    }

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

    @PostMapping("/{postId}/booked")
    private Response<Void> booked(@AuthenticationPrincipal LoginUser loginUser,
                                  @PathVariable Long postId,
                                  @RequestBody PostBookedRequest request){
        postWriteService.booked(loginUser.getId(), postId, request.getBookerId());
        return Response.success();
    }

    @DeleteMapping("/{postId}/booked")
    private Response<Void> cancelBooked(@AuthenticationPrincipal LoginUser loginUser,
                                        @PathVariable Long postId){
        postWriteService.cancelBooked(loginUser.getId(), postId);
        return Response.success();
    }

    @PostMapping("/{postId}/sold")
    private Response<Void> soldOut(@AuthenticationPrincipal LoginUser loginUser,
                                   @PathVariable Long postId){
        postWriteService.soldOut(loginUser.getId(), postId);
        return Response.success();
    }
}
