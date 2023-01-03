package com.carrot.application.like.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.post.domain.Post;
import com.carrot.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "post_like")
@NoArgsConstructor(access = PROTECTED)
public class PostLike extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public PostLike(Long id, Post post, User user) {
        this.id = id;
        this.post = post;
        this.user = user;
    }

    public static PostLike of(Post post, User user){
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }
}
