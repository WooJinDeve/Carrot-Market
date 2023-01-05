package com.carrot.application.article.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Entity
@Table(name = "article_id")
@NoArgsConstructor(access = PROTECTED)
public class Article extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User user;

    @Embedded
    private Sentence sentence;

    private LocalDateTime deletedAt;

    @Builder
    public Article(Long id, User user, Sentence sentence, LocalDateTime deletedAt) {
        this.id = id;
        this.user = user;
        this.sentence = sentence;
        this.deletedAt = deletedAt;
    }

    private boolean isDeleted(){
        return Objects.nonNull(deletedAt);
    }

    private void softDeleted(){
        this.deletedAt = LocalDateTime.now();
    }
}
