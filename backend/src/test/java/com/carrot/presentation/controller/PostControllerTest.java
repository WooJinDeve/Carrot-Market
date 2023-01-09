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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.carrot.presentation.request.PostRequest.*;
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
    void givenPostSaveRequest_whenSaving_thenSavePost() throws Exception {
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

    @DisplayName("[POST] 게시물 저장 요청시 제목이 50자를 초과할 경우 - 요청실패")
    @Test
    @WithMockUser
    public void givenPostSaveRequest_whenSaving_thenThrowValid() throws Exception {
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

    @DisplayName("[POST] 게시물 저장시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenPostSaveRequest_whenSaving_thenThrowNotLogin() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostSaveRequest fixture = getSaveRequest(title, content);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[PUT] 게시물 수정 요청 - 요청실패")
    @Test
    @WithMockUser
    public void givenPostUpdateRequest_whenUpdating_thenUpdatePost() throws Exception {
        //then
        Long userId = 1L;
        Long postId = 1L;
        String title = "title";
        String content = "content";
        PostUpdateRequest fixture = getUpdateRequest(title, content);

        //when
        doNothing().when(postWriteService).update(eq(userId),eq(postId), eq(fixture));

        final ResultActions perform = mockMvc.perform(put("/api/v1/posts/{postId}", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[PUT] 게시물 수정시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenPostUpdateRequest_whenUpdating_thenThrowNotLogin() throws Exception {
        //given
        Long postId = 1L;
        String title = "title";
        String content = "content";
        PostUpdateRequest fixture = getUpdateRequest(title, content);

        //when
        final ResultActions perform = mockMvc.perform(put("/api/v1/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fixture)));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[DELETE] 게시물 삭제 요청 - 요청성공")
    @Test
    @WithMockUser
    public void givenPostId_whenDeleting_thenDeletePost() throws Exception {
        //then
        Long userId = 1L;
        Long postId = 1L;

        //when
        doNothing().when(postWriteService).delete(eq(userId),eq(postId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/posts/{postId}", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[DELETE] 게시물 수정시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenPostId_whenDeleting_thenThrowNotLogin() throws Exception {
        //given
        Long postId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(delete("/api/v1/posts/{postId}", postId));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[POST] 게시물 예약 요청 - 요청 성공")
    @Test
    void givenPostBookedRequest_whenBooked_thenBookedPost() throws Exception {
        //given
        Long postId = 1L;
        Long bookerId = 2L;
        PostBookedRequest request = new PostBookedRequest(bookerId);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/booked", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 게시물 예약시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenPostBookedRequest_whenBooked_thenThrowNotLogin() throws Exception {
        //given
        Long postId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/booked", postId));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[DELETE] 게시물 예약 취소 요청 - 요청 성공")
    @Test
    @WithAnonymousUser
    void givenPostId_WhenDeletingBooked_thenDeleteBookedAndChangePostStatue() throws Exception {
        //given
        Long postId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(delete("/api/v1/posts/{postId}/booked", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[DELETE] 게시물 예약 취소시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenPostId_WhenDeletingBooked_thenThrowNotLogin() throws Exception {
        //given
        Long postId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(delete("/api/v1/posts/{postId}/booked", postId));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
