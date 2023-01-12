package com.carrot.application.chat.repository;

import com.carrot.application.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm JOIN FETCH cm.user WHERE cm.chatRoom.id = :chatRoomId")
    Slice<ChatMessage> findAllByChatRoomIdWithUserOrderByIdAsc(Long chatRoomId, Pageable pageable);
}
