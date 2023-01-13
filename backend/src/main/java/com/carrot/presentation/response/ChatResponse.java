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

import static com.carrot.presentation.response.UserResponse.UserProfileResponse;

public class ChatResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentChatMessageResponse implements Serializable {
        private Long chatRoomId;
        private Long chatId;
        private UserProfileResponse userProfile;
        private String message;
        private LocalDateTime createdAt;

        public static RecentChatMessageResponse of(ChatMessage chatMessage){
            return RecentChatMessageResponse.builder()
                    .chatRoomId(chatMessage.getChatRoom().getId())
                    .chatId(chatMessage.getId())
                    .userProfile(UserProfileResponse.of(chatMessage.getUser()))
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
        private boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageResponse {
        private Long chatId;
        private UserProfileResponse user;
        private String message;
        private LocalDateTime createdAt;

        public static ChatMessageResponse of(ChatMessage chatMessage){
            return ChatMessageResponse.builder()
                    .chatId(chatMessage.getId())
                    .user(UserProfileResponse.of(chatMessage.getUser()))
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
    public static class ChatRoomResponse {


        private Long chatRoomId;
        private UserProfileResponse seller;
        private UserProfileResponse buyer;

        public static ChatRoomResponse of(ChatRoom chatRoom) {
            return ChatRoomResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .seller(UserProfileResponse.of(chatRoom.getSeller()))
                    .buyer(UserProfileResponse.of(chatRoom.getBuyer()))
                    .build();
        }

    }
}