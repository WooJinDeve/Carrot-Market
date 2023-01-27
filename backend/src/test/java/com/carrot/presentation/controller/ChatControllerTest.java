package com.carrot.presentation.controller;

import com.carrot.application.chat.service.ChatReadService;
import com.carrot.application.chat.service.ChatWriteService;
import com.carrot.config.TestSecurityConfig;
import com.carrot.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.carrot.presentation.response.ChatResponse.ChatMessageResponses;
import static com.carrot.presentation.response.ChatResponse.ChatRoomResponses;
import static com.carrot.support.QueryParamUtil.QueryParam;
import static com.carrot.support.fixture.ChatFixture.getChatMessageResponses;
import static com.carrot.support.fixture.ChatFixture.getChatRoomResponses;
import static com.carrot.support.fixture.TokenFixture.AUTHORIZATION_HEADER_NAME;
import static com.carrot.support.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@DisplayName("[View] ChatController")
public class ChatControllerTest extends ControllerTest {

    @MockBean
    private ChatWriteService chatWriteService;

    @MockBean
    private ChatReadService chatReadService;

    @DisplayName("[POST] 채팅방 생성 - 요청성공")
    @Test
    @WithMockUser
    void givenBuyerIdAndPostId_whenSaving_thenSaveChatRoom() throws Exception {
        //then
        Long buyerId = 1L;
        Long postId = 1L;

        //when
        doNothing().when(chatWriteService).create(eq(buyerId), eq(postId));

        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/chatRooms", postId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 채팅방 생성시 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenPostId_whenSaving_thenThrowNotLogin() throws Exception {
        //given
        Long postId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/posts/{postId}/chatRooms", postId));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[DELETE] 채팅방 삭제 - 요청 성공")
    @Test
    @WithMockUser
    void givenChatRoomIdAndUserId_whenDeleting_thenDeletedChatRoom() throws Exception {
        //then
        Long chatRoomId = 1L;
        Long userId = 1L;

        //when
        doNothing().when(chatWriteService).delete(eq(userId), eq(chatRoomId));

        final ResultActions perform = mockMvc.perform(delete("/api/v1/chatRooms/{chatRoomId}", chatRoomId)
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("[DELETE] 채팅방 삭제시 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenChatRoomId_whenDeleting_thenThrowNotLogin() throws Exception {
        //given
        Long chatRoomId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(delete("/api/v1/chatRooms/{chatRoomId}", chatRoomId));
        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[GET] 채팅방 전체 조회 - 요청 성공")
    @Test
    @WithAnonymousUser
    void givenUserId_whenFinding_thenFindChatRoomList() throws Exception {
        //given
        Long userId = 1L;

        ChatRoomResponses fixture = getChatRoomResponses(userId, 20L, true);

        //when
        when(chatReadService.getPageChatRoom(eq(userId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/chatRooms")
                .params(QueryParam())
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("[GET] 채팅방 전체 조회시, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void whenFinding_thenThrowNotLogin() throws Exception {
        //when
        final ResultActions perform = mockMvc.perform(get("/api/v1/chatRooms"));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("[[GET] 채팅방 내역 조회 - 요청 성공")
    @Test
    @WithAnonymousUser
    void givenChatRoomId_whenFinding_thenFindMessageList() throws Exception {
        //given
        Long chatRoomId = 1L;
        Long userId = 1L;

        ChatMessageResponses fixture = getChatMessageResponses(chatRoomId, userId, 20L, true);

        //when
        when(chatReadService.getByChatRoomIdWithMessage(eq(chatRoomId), any())).thenReturn(fixture);

        final ResultActions perform = mockMvc.perform(get("/api/v1/chatRooms/{chatRoomId}/messages", chatRoomId)
                .params(QueryParam())
                .header(AUTHORIZATION_HEADER_NAME, BEARER_TOKEN));

        //then
        perform.andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("[GET] 채팅방 내역 조회, 로그인하지 않은 경우 - 요청 실패")
    @Test
    @WithAnonymousUser
    void givenChatRoomId_whenFinding_thenThrowNotLogin() throws Exception {
        //given
        Long chatRoomId = 1L;

        //when
        final ResultActions perform = mockMvc.perform(get("/api/v1/chatRooms/{chatRoomId}/messages", chatRoomId));

        //then
        perform.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
