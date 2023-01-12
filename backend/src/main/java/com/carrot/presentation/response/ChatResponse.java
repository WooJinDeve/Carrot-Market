package com.carrot.presentation.response;

import com.carrot.application.chat.domain.ChatMessage;
import com.carrot.application.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ChatResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentChatMessageResponse implements Serializable {
        private Long chatRoomId;
        private Long chatId;
        private Long userId;
        private String message;
        private LocalDateTime createdAt;

        public static RecentChatMessageResponse of(ChatMessage chatMessage){
            return RecentChatMessageResponse.builder()
                    .chatRoomId(chatMessage.getChatRoom().getId())
                    .chatId(chatMessage.getId())
                    .userId(chatMessage.getUser().getId())
                    .message(chatMessage.getMessage())
                    .createdAt(chatMessage.getCreatedAt())
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageResponses{
        private Long chatRoomId;
        private List<ChatMessageResponse> responses;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageResponse {
        private Long chatId;
        private Long userId;
        private String message;
        private LocalDateTime createdAt;

        public static ChatMessageResponse of(ChatMessage chatMessage){
            return ChatMessageResponse.builder()
                    .chatId(chatMessage.getId())
                    .userId(chatMessage.getUser().getId())
                    .message(chatMessage.getMessage())
                    .createdAt(chatMessage.getCreatedAt())
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomResponses{
        List<ChatRoomResponse> responses;
        private boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomResponse{
        private Long chatRoomId;
        private Long sellerId;
        private Long buyerId;

        public static ChatRoomResponse of(ChatRoom chatRoom){
            return ChatRoomResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .sellerId(chatRoom.getSeller().getId())
                    .buyerId(chatRoom.getBuyer().getId())
                    .build();
        }
    }
}