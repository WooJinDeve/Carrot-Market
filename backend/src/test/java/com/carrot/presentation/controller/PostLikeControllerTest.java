package com.carrot.presentation.controller;

import com.carrot.application.like.service.PostLikeReadService;
import com.carrot.application.like.service.PostLikeWriteService;
import com.carrot.config.TestSecurityConfig;
import com.carrot.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.carrot.support.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.support.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@DisplayName("[View] PostLikeController")
public class PostLikeControllerTest extends ControllerTest {


    @MockBean
    private PostLikeWriteService postLikeWriteService;

    @MockBean
    private PostLikeReadService postLikeReadService;


    @DisplayName("[POST] 게시물 좋아요 - 요청성공")
    @Test
    @WithMockUser
    void 게시물_좋아요 () throws Exception {
        //then
        Long userId = 1L;
        Long postId = 1L;

        //when
        doNothing().when(postLikeWriteService).like(eq(userId), eq(postId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/like", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 게시물 좋아요 요청시, 로그인 하지않은 유저의 경우 - 요청실패")
    @Test
    @WithAnonymousUser
    void 게시물_좋아요시_로그인_하지않은_유저의_경우() throws Exception {
        //then
        Long postId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/like", postId));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[DELETE] 게시물 좋아요 취소 - 요청 성공")
    @Test
    @WithMockUser
    void 게시물_좋아요_취소() throws Exception {
        //then
        Long userId = 1L;
        Long postId = 1L;

        //when
        doNothing().when(postLikeWriteService).cancel(eq(userId), eq(postId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/posts/{postId}/like", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[DELETE] 게시물 좋아요 취소시, 로그인 하지않은 유저의 경우 - 요청실패")
    @Test
    @WithAnonymousUser
    void 게시물_좋아요_취소시_로그인_하지않은_유저의_경우() throws Exception {
        //then
        Long postId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(delete("/api/v1/posts/{postId}/like", postId));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
