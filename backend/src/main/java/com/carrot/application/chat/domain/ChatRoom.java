package com.carrot.application.chat.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Entity
@Table(name = "chat_room")
@SQLDelete(sql = "UPDATE USER SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User seller;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User buyer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = CASCADE)
    private Post post;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    public ChatRoom(Long id, User seller, User buyer, Post post, LocalDateTime deletedAt) {
        this.id = id;
        this.seller = seller;
        this.buyer = buyer;
        this.post = post;
        this.deletedAt = deletedAt;
    }
}
