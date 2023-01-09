package com.carrot.application.post.domain.entity;

import com.carrot.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(name = "transaction_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "th_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User seller;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String thumbnail;

    @Builder
    public TransactionHistory(Long id, User buyer, User seller, Post post, String thumbnail) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.post = post;
        this.thumbnail = thumbnail;
    }
}