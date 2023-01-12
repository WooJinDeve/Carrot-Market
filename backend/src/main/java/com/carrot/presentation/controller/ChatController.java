package com.carrot.presentation.controller;

import com.carrot.application.chat.service.ChatReadService;
import com.carrot.application.chat.service.ChatWriteService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.carrot.presentation.response.ChatResponse.ChatMessageResponses;
import static com.carrot.presentation.response.ChatResponse.ChatRoomResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatReadService chatReadService;
    private final ChatWriteService chatWriteService;

    @PostMapping("/posts/{postId}/chatRooms")
    public Response<Void> create(@PathVariable Long postId,
                                 @AuthenticationPrincipal LoginUser loginUser) {
        chatWriteService.create(loginUser.getId(), postId);
        return Response.success();
    }

    @GetMapping("/chatRooms")
    public Response<ChatRoomResponses> getAllChatRoom(@AuthenticationPrincipal LoginUser loginUser,
                                                      Pageable pageable) {
        ChatRoomResponses responses = chatReadService.getPageChatRoom(loginUser.getId(), pageable);
        return Response.success(responses);
    }


    @GetMapping("/chatRooms/{chatRoomId}/messages")
    public Response<ChatMessageResponses> getByMessage(@PathVariable Long chatRoomId,
                                                       @AuthenticationPrincipal LoginUser loginUser,
                                                       Pageable pageable) {
        ChatMessageResponses responses = chatReadService.getByChatRoomIdWithMessage(chatRoomId, pageable);
        return Response.success(responses);
    }

    @DeleteMapping("/chatRooms/{chatRoomId}")
    public Response<Void> delete(@PathVariable Long chatRoomId,
                                 @AuthenticationPrincipal LoginUser loginUser){
        chatWriteService.delete(loginUser.getId(), chatRoomId);
        return Response.success();
    }
}
