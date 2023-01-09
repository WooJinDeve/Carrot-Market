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
@Table(name = "booked_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BookedHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "bh_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User seller;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User booker;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = CASCADE)
    private Post post;

    @Column(nullable = false)
    private String thumbnail;

    @Builder
    public BookedHistory(Long id, User seller, User booker, Post post, String thumbnail) {
        this.id = id;
        this.seller = seller;
        this.booker = booker;
        this.post = post;
        this.thumbnail = thumbnail;
    }

    public static BookedHistory of(User seller, User booker, Post post, String thumbnail) {
        return BookedHistory.builder()
                .seller(seller)
                .booker(booker)
                .post(post)
                .thumbnail(thumbnail)
                .build();
    }
}
