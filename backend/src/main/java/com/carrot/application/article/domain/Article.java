package com.carrot.application.article.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.carrot.global.error.ErrorCode.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Entity
@Table(name = "article")
@NoArgsConstructor(access = PROTECTED)
public class Article extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = CASCADE)
    private Post post;

    @Embedded
    private Sentence sentence;

    private LocalDateTime deletedAt;

    @Builder
    public Article(Long id, User user, Post post, Sentence sentence, LocalDateTime deletedAt) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.sentence = sentence;
        this.deletedAt = deletedAt;
    }

    public static Article of(User user, Post post, String sentence){
        return Article.builder()
                .user(user)
                .post(post)
                .sentence(new Sentence(sentence))
                .build();
    }

    public void verifyOwner(Long userId){
        if (!Objects.equals(user.getId(), userId)) {
            throw new CarrotRuntimeException(ARTICLE_VALIDATION_ERROR);
        }
    }

    public void change(String sentence){
        this.sentence = new Sentence(sentence);
    }

    public boolean isDeleted(){
        return Objects.nonNull(deletedAt);
    }

    public void verifySoftDeleted(){
        if (isDeleted())
            throw new CarrotRuntimeException(ARTICLE_NOTFOUND_ERROR);
    }

    public void softDeleted(){
        this.deletedAt = Objects.isNull(this.deletedAt)? LocalDateTime.now() : this.deletedAt;
    }
}
