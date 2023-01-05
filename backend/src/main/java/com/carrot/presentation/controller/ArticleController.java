package com.carrot.presentation.controller;

import com.carrot.application.article.service.ArticleReadService;
import com.carrot.application.article.service.ArticleWriteService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.carrot.presentation.request.ArticleDto.ArticleSaveRequest;
import static com.carrot.presentation.request.ArticleDto.ArticleUpdateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleController {

    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;


    @PostMapping("/posts/{postId}/article")
    public Response<Void> saveArticle(@PathVariable Long postId,
                                      @AuthenticationPrincipal LoginUser loginUser,
                                      @RequestBody @Valid ArticleSaveRequest request) {

        articleWriteService.saveArticle(loginUser.getId(), postId, request);
        return Response.success();
    }

    @PostMapping("/article/{articleId}/reply")
    public Response<Void> saveReply(@PathVariable Long articleId,
                                    @AuthenticationPrincipal LoginUser loginUser,
                                    @RequestBody @Valid ArticleSaveRequest request) {

        articleWriteService.saveReply(loginUser.getId(), articleId, request);
        return Response.success();
    }

    @PutMapping("/article/{articleId}")
    public Response<Void> updateArticle(@PathVariable Long articleId,
                                        @AuthenticationPrincipal LoginUser loginUser,
                                        @RequestBody @Valid ArticleUpdateRequest request){
        articleWriteService.updateArticle(loginUser.getId(), articleId, request);
        return Response.success();
    }

    @PutMapping("/reply/{replyId}")
    public Response<Void> updateReply(@PathVariable Long replyId,
                                        @AuthenticationPrincipal LoginUser loginUser,
                                        @RequestBody @Valid ArticleUpdateRequest request){
        articleWriteService.updateReply(loginUser.getId(), replyId, request);
        return Response.success();
    }

    @DeleteMapping("/article/{articleId}")
    public Response<Void> deleteArticle(@PathVariable Long articleId,
                                        @AuthenticationPrincipal LoginUser loginUser){
        articleWriteService.deleteArticle(loginUser.getId(), articleId);
        return Response.success();
    }

    @DeleteMapping("/reply/{replyId}")
    public Response<Void> deleteReply(@PathVariable Long replyId,
                                        @AuthenticationPrincipal LoginUser loginUser){
        articleWriteService.deleteReply(loginUser.getId(), replyId);
        return Response.success();
    }
}
