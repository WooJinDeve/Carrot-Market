package com.carrot.presentation.controller;

import com.carrot.application.post.service.PostReadService;
import com.carrot.application.post.service.PostWriteService;
import com.carrot.config.TestSecurityConfig;
import com.carrot.testutil.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.carrot.presentation.request.PostRequest.PostSaveRequest;
import static com.carrot.presentation.request.PostRequest.PostUpdateRequest;
import static com.carrot.testutil.fixture.PostFixture.getSaveRequest;
import static com.carrot.testutil.fixture.PostFixture.getUpdateRequest;
import static com.carrot.testutil.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.testutil.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@DisplayName("[View] PostController")
public class PostControllerTest extends ControllerTest {

    @MockBean
    private PostWriteService postWriteService;

    @MockBean
    private PostReadService postReadService;

    @DisplayName("[POST] 게시물 저장 - 요청성공")
    @Test
    @WithMockUser
    void 게시물_저장() throws Exception {
        //then
        Long userId = 1L;
        String title = "title";
        String content = "content";
        PostSaveRequest fixture = getSaveRequest(title, content);

        //when
        when(postWriteService.createPost(eq(userId), eq(fixture))).thenReturn(anyLong());

        final ResultActions perform = mockMvc.perform(post("/api/v1/posts")
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 게시물 요청시 제목이 50자를 초과할 경우 - 요청실패")
    @Test
    @WithMockUser
    public void 게시물_요청시_제목이_50자를_초과할_경우() throws Exception {
        //then
        String title = "title".repeat(11);
        String content = "content";
        PostSaveRequest fixture = getSaveRequest(title, content);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts")
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("[POST] 게시물 수정 요청 - 요청실패")
    @Test
    @WithMockUser
    public void 게시물_수정_요청() throws Exception {
        //then
        Long userId = 1L;
        Long postId = 1L;
        String title = "title";
        String content = "content";
        PostUpdateRequest fixture = getUpdateRequest(title, content);

        //when
        doNothing().when(postWriteService).update(eq(userId),eq(postId), eq(fixture));

        final ResultActions perform = mockMvc.perform(put("/api/v1/posts/{regionId}", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[DELETE] 게시물 삭제 요청 - 요청성공")
    @Test
    @WithMockUser
    public void 게시물_삭제_요청() throws Exception {
        //then
        Long userId = 1L;
        Long postId = 1L;

        //when
        doNothing().when(postWriteService).delete(eq(userId),eq(postId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/posts/{regionId}", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }
}
