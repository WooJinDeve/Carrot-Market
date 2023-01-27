package com.carrot.support.fixture;

import com.carrot.application.chat.domain.ChatMessage;
import com.carrot.application.chat.domain.ChatRoom;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.carrot.presentation.response.ChatResponse.*;
import static com.carrot.presentation.response.UserResponse.UserProfileResponse;
import static java.time.LocalDateTime.now;

public class ChatFixture {
    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";

    public static ChatRoom get(Long id, User seller, User buyer, Post post){
        return ChatRoom.builder()
                .id(id)
                .seller(seller)
                .buyer(buyer)
                .post(post)
                .build();
    }

    public static ChatRoom getDeleted(Long id, User seller, User buyer, Post post){
        return ChatRoom.builder()
                .id(id)
                .seller(seller)
                .buyer(buyer)
                .post(post)
                .deletedAt(now())
                .build();
    }

    public static ChatMessage get(Long id, ChatRoom chatRoom, User user, String message) {
        return ChatMessage.builder()
                .id(id)
                .chatRoom(chatRoom)
                .user(user)
                .message(message)
                .build();
    }


    public static ChatMessageResponses getChatMessageResponses(Long chatRoomId, Long userId, Long size, boolean hasNext){
        return ChatMessageResponses.builder()
                .chatRoomId(chatRoomId)
                .responses(getChatMessageResponses(size, userId))
                .hasNext(hasNext)
                .build();
    }

    private static List<ChatMessageResponse> getChatMessageResponses(Long size, Long userId){
        return LongStream.range(1, size + 1)
                .mapToObj(i -> getChatMessageResponse(i, userId))
                .collect(Collectors.toList());
    }

    public static ChatMessageResponse getChatMessageResponse(Long id, Long userId){
        return ChatMessageResponse.builder()
                .chatId(id)
                .user(UserProfileResponse.of(UserFixture.get(userId)))
                .message(DEFAULT_MESSAGE)
                .createdAt(now())
                .build();
    }


    public static ChatRoomResponses getChatRoomResponses(Long userId, Long size, boolean hasNext){
        return ChatRoomResponses.builder()
                .responses(getChatRoomResponses(size, userId))
                .hasNext(hasNext)
                .build();
    }

    private static List<ChatRoomResponse> getChatRoomResponses(Long size, Long userId){
        return LongStream.range(1, size + 1)
                .mapToObj(i -> getChatRoomResponse(i, userId, i+1))
                .collect(Collectors.toList());
    }

    public static ChatRoomResponse getChatRoomResponse(Long id, Long senderId, Long buyerId){
        return ChatRoomResponse.builder()
                .chatRoomId(id)
                .seller(UserProfileResponse.of(UserFixture.get(senderId)))
                .buyer(UserProfileResponse.of(UserFixture.get(buyerId)))
                .build();
    }
}
