package com.carrot.presentation.controller;

import com.carrot.application.article.service.ArticleReadService;
import com.carrot.application.article.service.ArticleWriteService;
import com.carrot.config.TestSecurityConfig;
import com.carrot.testutil.ControllerTest;
import com.carrot.testutil.fixture.ArticleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.carrot.presentation.request.ArticleRequest.ArticleSaveRequest;
import static com.carrot.presentation.request.ArticleRequest.ArticleUpdateRequest;
import static com.carrot.presentation.response.ArticleResponse.ArticleResponses;
import static com.carrot.presentation.response.ArticleResponse.ReplyResponses;
import static com.carrot.testutil.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.testutil.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@DisplayName("[View] ArticleController")
class ArticleControllerTest extends ControllerTest {

    @MockBean
    private ArticleWriteService articleWriteService;

    @MockBean
    private ArticleReadService articleReadService;

    @DisplayName("[GET] 댓글 조회 - 요청성공")
    @Test
    @WithMockUser
    void givenPostId_whenFinding_thenFindArticleList() throws Exception {
        //given
        Long postId = 1L;
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("size", "20");
        query_param.add("page", "0");

        ArticleResponses fixture = ArticleFixture.getArticleResponses(20, true);

        //when
        when(articleReadService.getArticles(eq(postId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/posts/{postId}/article", postId)
                .params(query_param)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 댓글 조회시, 로그인하지 않은 경우 - 요청성공")
    @Test
    @WithAnonymousUser
    void givenPostId_whenFinding_thenThrowNotLogin() throws Exception {
        //given
        Long postId = 1L;
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("size", "20");
        query_param.add("page", "0");

        ArticleResponses fixture = ArticleFixture.getArticleResponses(20, true);

        //when
        when(articleReadService.getArticles(eq(postId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/posts/{postId}/article", postId)
                .params(query_param));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[GET] 대댓글 조회 - 요청성공")
    @Test
    @WithMockUser
    void givenArticleId_whenFinding_thenFindReplyList() throws Exception {
        //given
        Long articleId = 1L;
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("size", "20");
        query_param.add("page", "0");

        ReplyResponses fixture = ArticleFixture.getReplyResponses(20, true);

        //when
        when(articleReadService.getReplies(eq(articleId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/article/{articleId}/reply", articleId)
                .params(query_param)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 대댓글 조회시, 로그인 하지않은 경우 - 요청성공")
    @Test
    @WithAnonymousUser
    void givenArticleId_whenFinding_thenThrowNotLogin() throws Exception {
        //given
        Long articleId = 1L;
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("size", "20");
        query_param.add("page", "0");

        ReplyResponses fixture = ArticleFixture.getReplyResponses(20, true);

        //when
        when(articleReadService.getReplies(eq(articleId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/article/{articleId}/reply", articleId)
                .params(query_param));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[POST] 댓글 저장 - 요청성공")
    @Test
    @WithMockUser
    void givenArticleSaveRequest_whenSaving_thenSaveArticle() throws Exception {
        //given
        Long userId = 1L;
        Long postId = 1L;
        String sentence = "댓글입니다.";
        ArticleSaveRequest fixture = ArticleFixture.getSaveRequest(sentence);

        //when
        doNothing().when(articleWriteService).saveArticle(eq(userId), eq(postId), eq(fixture));

        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/article", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 댓글 저장시, 댓글의 길이가 255자를 초과할 경우 - 요청 실패")
    @Test
    @WithMockUser
    void givenArticleSaveRequest_whenSaving_thenThrowValidRequestBody() throws Exception {
        //given
        Long postId = 1L;
        String sentence = "a".repeat(256);
        ArticleSaveRequest fixture = ArticleFixture.getSaveRequest(sentence);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/article", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("[POST] 댓글 저장시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenArticleSaveRequest_whenSaving_thenThrowNotLogin() throws Exception {
        //given
        Long postId = 1L;
        String sentence = "댓글입니다.";
        ArticleSaveRequest fixture = ArticleFixture.getSaveRequest(sentence);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/article", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[POST] 대댓글 저장 - 요청성공")
    @Test
    @WithMockUser
    void givenArticleSaveRequest_whenSaving_thenSaveReply() throws Exception {
        //given
        Long userId = 1L;
        Long articleId = 1L;
        String sentence = "댓글입니다.";
        ArticleSaveRequest fixture = ArticleFixture.getSaveRequest(sentence);

        //when
        doNothing().when(articleWriteService).saveReply(eq(userId), eq(articleId), eq(fixture));

        final ResultActions perform = mockMvc.perform(post("/api/v1/article/{articleId}/reply", articleId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 대댓글 저장시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenArticleSaveRequest_whenSaving_thenThrowNotLoginReply() throws Exception {
        Long articleId = 1L;
        String sentence = "댓글입니다.";
        ArticleSaveRequest fixture = ArticleFixture.getSaveRequest(sentence);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/article/{articleId}/reply", articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("[PUT] 댓글 정보 수정 - 요청 성공")
    @Test
    @WithMockUser
    void givenArticleUpdateRequest_whenUpdating_thenUpdateArticle() throws Exception {
        //given
        Long userId = 1L;
        Long articleId = 1L;
        String sentence = "수정댓글입니다.";
        ArticleUpdateRequest fixture = ArticleFixture.getUpdateRequest(sentence);

        //when
        doNothing().when(articleWriteService).updateArticle(eq(userId), eq(articleId), eq(fixture));

        final ResultActions perform = mockMvc.perform(put("/api/v1/article/{articleId}", articleId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[PUT] 댓글 정보 수정시, 수정 정보가 255자를 초과한 경우 - 요청 실패")
    @Test
    @WithMockUser
    void givenArticleUpdateRequest_whenUpdating_thenThrowValidRequestBody() throws Exception {
        //given
        Long articleId = 1L;
        String sentence = "a".repeat(256);
        ArticleUpdateRequest fixture = ArticleFixture.getUpdateRequest(sentence);

        //when
        final ResultActions perform = mockMvc.perform(put("/api/v1/article/{articleId}", articleId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("[PUT] 댓글 수정시, 로그인 하지않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenArticleUpdateRequest_whenUpdating_thenThrowNotLogin() throws Exception {
        //given
        Long articleId = 1L;
        String sentence = "수정댓글입니다.";
        ArticleUpdateRequest fixture = ArticleFixture.getUpdateRequest(sentence);

        //when
        final ResultActions perform = mockMvc.perform(put("/api/v1/article/{articleId}", articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[PUT] 대댓글 정보 수정 - 요청 성공")
    @Test
    @WithMockUser
    void givenArticleUpdateRequest_whenUpdating_thenUpdateReply() throws Exception {
        //given
        Long userId = 1L;
        Long replyId = 1L;
        String sentence = "수정댓글입니다.";
        ArticleUpdateRequest fixture = ArticleFixture.getUpdateRequest(sentence);

        //when
        doNothing().when(articleWriteService).updateReply(eq(userId), eq(replyId), eq(fixture));

        final ResultActions perform = mockMvc.perform(put("/api/v1/reply/{replyId}", replyId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("[PUT] 대댓글 수정시, 로그인 하지않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenArticleUpdateRequest_whenUpdating_thenThrowNotLoginReply() throws Exception {
        //given
        Long replyId = 1L;
        String sentence = "수정댓글입니다.";
        ArticleUpdateRequest fixture = ArticleFixture.getUpdateRequest(sentence);

        //when
        final ResultActions perform = mockMvc.perform(put("/api/v1/reply/{replyId}", replyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[DELETE] 댓글 삭제 요청 - 요청 성공")
    @Test
    @WithMockUser
    void givenArticleId_whenDeleting_thenDeleteArticle() throws Exception {
        //given
        Long userId = 1L;
        Long articleId = 1L;

        //when
        doNothing().when(articleWriteService).deleteArticle(eq(userId), eq(articleId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/article/{articleId}", articleId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[DELETE] 대댓글 삭제 요청 - 요청 성공")
    @Test
    @WithMockUser
    void givenReplyId_whenDeleting_thenDeleteArticle() throws Exception {
        //given
        Long userId = 1L;
        Long replyId = 1L;

        //when
        doNothing().when(articleWriteService).deleteReply(eq(userId), eq(replyId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/reply/{replyId}", replyId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }
}