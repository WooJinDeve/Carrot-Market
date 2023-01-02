package com.carrot.application.post.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.region.domain.Region;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.carrot.global.error.ErrorCode.*;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Embedded
    private Content content;

    @Column(name = "hits", nullable = false)
    private Integer hits;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Enumerated(STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "chat_num", nullable = false)
    private Integer chatNum;

    @Column(name = "article_num", nullable = false)
    private Integer articleNum;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @Builder
    public Post(Long id, User user, Region region, Integer hits, String thumbnail, Category category,
                Integer chatNum, Integer articleNum, LocalDateTime deletedAt) {
        this.id = id;
        this.user = user;
        this.region = region;
        this.hits = hits;
        this.thumbnail = thumbnail;
        this.category = category;
        this.chatNum = chatNum;
        this.articleNum = articleNum;
        this.deletedAt = deletedAt;
    }

    private void verifyOverflowHits(Integer hits){
        this.hits += hits;
        if (this.hits < 0)
            throw new CarrotRuntimeException(POST_HITS_OVERFLOW_ERROR);
    }

    public void verifySoftDeleted(){
        if (Objects.nonNull(deletedAt))
            throw new CarrotRuntimeException(POST_NOTFOUND_ERROR);
    }

    public void verifyOwner(Long userId){
        if (!Objects.equals(this.user.getId(), userId))
            throw new CarrotRuntimeException(POST_VALIDATION_ERROR);
    }
}
