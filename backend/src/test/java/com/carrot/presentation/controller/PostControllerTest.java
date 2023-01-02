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

import static com.carrot.presentation.request.PostDto.PostSaveRequest;
import static com.carrot.testutil.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.testutil.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        //given
        PostSaveRequest mockRequest = mock(PostSaveRequest.class);
        Long postId = 1L;
        Long userId = 1L;

        //when
        when(postWriteService.createPost(eq(userId), eq(mockRequest))).thenReturn(postId);

        final ResultActions perform = mockMvc.perform(get("/api/v1/posts")
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }
}
