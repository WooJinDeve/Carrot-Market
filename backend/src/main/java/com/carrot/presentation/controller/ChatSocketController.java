package com.carrot.presentation.controller;

import com.carrot.application.chat.service.ChatWriteService;
import com.carrot.global.common.Response;
import com.carrot.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import static com.carrot.presentation.request.ChatRequest.ChatSendMessage;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatWriteService chatService;
    @MessageMapping("/chatRooms/{id}")
    public void send(@DestinationVariable Long id, ChatSendMessage chatSendMessage) {
        chatService.send(id, chatSendMessage.getUserId(), chatSendMessage.getContent());
    }

    @MessageExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
        log.error("[Error] Internal Server Error ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }
}
