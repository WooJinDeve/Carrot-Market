package com.carrot.application.chat.repository;

import com.carrot.application.chat.domain.ChatRoom;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.global.error.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.buyer JOIN FETCH cr.seller WHERE cr.seller.id = :userId OR cr.buyer.id = :userId")
    Slice<ChatRoom> findAllBySenderIdOrReceiverIdOrderByUpdatedAt(Long userId, Pageable pageable);

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.post WHERE cr.id = :id")
    Optional<ChatRoom> findByIdWithPost(Long id);

    default ChatRoom getByIdWithPost(Long id){
        return findByIdWithPost(id)
                .orElseThrow(() -> new CarrotRuntimeException(ErrorCode.CHATROOM_NOTFOUND_ERROR));
    }
    default ChatRoom getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(ErrorCode.CHATROOM_NOTFOUND_ERROR));
    }
}
