package com.carrot.application.article.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.Objects;

import static com.carrot.global.error.ErrorCode.ARTICLE_VALIDATION_ERROR;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Entity
@Table(name = "reply")
@NoArgsConstructor(access = PROTECTED)
public class Reply extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Embedded
    private Sentence sentence;

    @Builder
    public Reply(Long id, User user, Article article, Sentence sentence) {
        this.id = id;
        this.user = user;
        this.article = article;
        this.sentence = sentence;
    }

    public static Reply of(User user, Article article, String sentence){
        return Reply.builder()
                .user(user)
                .article(article)
                .sentence(new Sentence(sentence))
                .build();
    }

    public void change(String sentence){
        this.sentence = new Sentence(sentence);
    }

    public void verifyOwner(Long userId){
        if (!Objects.equals(user.getId(), userId)) {
            throw new CarrotRuntimeException(ARTICLE_VALIDATION_ERROR);
        }
    }
}
