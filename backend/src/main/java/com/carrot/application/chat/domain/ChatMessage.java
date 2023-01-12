package com.carrot.application.chat.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Entity
@Table(name = "chat_message")
@NoArgsConstructor(access = PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    @OnDelete(action = CASCADE)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "message", nullable = false)
    private String message;

    @Builder
    public ChatMessage(Long id, ChatRoom chatRoom, User user, String message) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.user = user;
        this.message = message;
    }

    public static ChatMessage of(ChatRoom chatRoom, User user, String message){
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .user(user)
                .message(message)
                .build();
    }
}
