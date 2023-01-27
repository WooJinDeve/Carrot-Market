package com.carrot.presentation.response;

import com.carrot.application.post.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListResponses {
        private List<PostListResponse> response;
        private boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListResponse {

        private Long postId;
        private String regionName;
        private String title;
        private Integer price;
        private String statue;
        private String thumbnail;
        private String category;
        private Integer hitsCount;
        private Integer chatCount;
        private Integer articleCount;
        private Integer likeCount;
        private LocalDateTime createdAt;

        @Builder
        public PostListResponse(final Post post) {
            this.postId = post.getId();
            this.regionName = post.getRegion().getName();
            this.title = post.getContent().getTitle();
            this.price = post.getContent().getPrice();
            this.statue = post.getStatus().getState();
            this.thumbnail = post.getThumbnail();
            this.category = post.getCategory().getName();
            this.hitsCount = post.getHits();
            this.chatCount = post.getChatNum();
            this.articleCount = post.getArticleNum();
            this.likeCount = post.getLikeNum();
            this.createdAt = post.getCreatedAt();
        }
    }
}
