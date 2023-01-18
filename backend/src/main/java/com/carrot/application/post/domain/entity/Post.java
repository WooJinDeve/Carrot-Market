package com.carrot.application.post.domain.entity;

import com.carrot.application.common.BaseEntity;
import com.carrot.application.post.domain.Category;
import com.carrot.application.post.domain.Content;
import com.carrot.application.post.domain.PostStatue;
import com.carrot.application.region.domain.Region;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.carrot.application.post.domain.PostStatue.*;
import static com.carrot.global.error.ErrorCode.*;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "post")
@Getter
@SQLDelete(sql = "UPDATE POST SET deleted_at = NOW() WHERE id = ?")
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

    @Enumerated(STRING)
    private PostStatue status;

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
    public Post(Long id, User user, Region region, Content content, PostStatue status, Integer hits,
                String thumbnail, Category category, Integer chatNum, Integer articleNum, LocalDateTime deletedAt) {
        this.id = id;
        this.user = user;
        this.region = region;
        this.content = content;
        this.status = Objects.isNull(status) ? SALE : status;
        this.hits = Objects.isNull(hits) ? 0 : hits;
        this.thumbnail = thumbnail;
        this.category = category;
        this.chatNum = Objects.isNull(chatNum) ? 0 : chatNum;
        this.articleNum = Objects.isNull(articleNum) ? 0 : articleNum;
        this.deletedAt = deletedAt;
    }

    /* create post*/
    public static Post of(User user, Region region, String title, String content,
                          Integer price, Category category, String thumbnail){
        return Post.builder()
                .user(user)
                .region(region)
                .content(new Content(title, content, price))
                .category(category)
                .thumbnail(thumbnail)
                .build();
    }

    public void update(String title, String content, Integer price, Category category, String thumbnail) {
        this.content = new Content(title, content ,price);
        this.category = category;
        this.thumbnail = thumbnail;
    }

    public void addPostImages(PostImage postImage){
        if (this.postImages.contains(postImage))
            postImages.remove(postImage);
        this.postImages.add(postImage);
        postImage.addPost(this);
    }


    public void verifyOverflowHits(Integer hits){
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

    public void verifyAndStatueChangeSale(){
        if (this.status != BOOKED)
            throw new CarrotRuntimeException(POST_VALIDATION_ERROR);
        this.status = SALE;
    }

    public void verifyAndStatueChangeBooked(){
        if (this.status != SALE)
            throw new CarrotRuntimeException(POST_VALIDATION_ERROR);
        this.status = BOOKED;
    }

    public void verifyAndStatueChangeSoldOut(){
        if (this.status != BOOKED)
            throw new CarrotRuntimeException(POST_VALIDATION_ERROR);
        this.status = SOLD_OUT;
    }
}
