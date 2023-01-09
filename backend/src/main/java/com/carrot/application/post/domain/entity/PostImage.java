package com.carrot.application.post.domain.entity;

import com.carrot.application.common.BaseEntity;
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
@Table(name = "post_image")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PostImage extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Builder
    public PostImage(Long id, Post post, String originName, String imageUrl) {
        this.id = id;
        this.post = post;
        this.originName = originName;
        this.imageUrl = imageUrl;
    }

    public static PostImage of(String originName, String imageUrl){
        return PostImage.builder()
                .originName(originName)
                .imageUrl(imageUrl)
                .build();
    }

    public void addPost(Post post) {
        this.post = post;
    }
}
