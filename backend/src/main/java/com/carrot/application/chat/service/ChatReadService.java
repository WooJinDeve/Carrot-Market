package com.carrot.application.chat.service;

import com.carrot.application.chat.domain.ChatMessage;
import com.carrot.application.chat.domain.ChatRoom;
import com.carrot.application.chat.repository.ChatMessageRepository;
import com.carrot.application.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.presentation.response.ChatResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatReadService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;


    public ChatMessageResponses getByChatRoomIdWithMessage(Long chatRoomId, Pageable pageable) {
        Slice<ChatMessage> chatMessages = chatMessageRepository
                .findAllByChatRoomIdWithUserOrderByIdAsc(chatRoomId, pageable);

        return ChatMessageResponses.builder()
                .chatRoomId(chatRoomId)
                .responses(convertToChatMessageResponse(chatMessages.getContent()))
                .hasNext(chatMessages.hasNext())
                .build();
    }

    private List<ChatMessageResponse> convertToChatMessageResponse(List<ChatMessage> messages) {
        return messages.stream()
                .map(ChatMessageResponse::of)
                .collect(Collectors.toList());
    }

    public ChatRoomResponses getPageChatRoom(Long userId, Pageable pageable) {
        Slice<ChatRoom> chatRooms = chatRoomRepository.findAllBySenderIdOrReceiverIdOrderByUpdatedAt(userId, pageable);

        return ChatRoomResponses.builder()
                .responses(convertToChatRoomResponse(chatRooms.getContent()))
                .hasNext(chatRooms.hasNext())
                .build();
    }

    private List<ChatRoomResponse> convertToChatRoomResponse(List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .filter(chatRoom -> !chatRoom.isDeleted())
                .map(ChatRoomResponse::of)
                .collect(Collectors.toList());
    }
}
