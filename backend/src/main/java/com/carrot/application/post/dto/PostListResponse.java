package com.carrot.application.post.dto;

import com.carrot.application.post.domain.Category;
import com.carrot.application.post.domain.PostStatue;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponse {

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
    private LocalDateTime localDateTime;

    @Builder
    public PostListResponse(final Long postId, final String regionName, final String title, final Integer price,
                            final PostStatue statue, final String thumbnail, final Category category, final Integer hitsCount,
                            final Integer chatCount, final Integer articleCount, final Integer likeCount,
                            final LocalDateTime localDateTime) {
        this.postId = postId;
        this.regionName = regionName;
        this.title = title;
        this.price = price;
        this.statue = statue.getState();
        this.thumbnail = thumbnail;
        this.category = category.getName();
        this.hitsCount = hitsCount;
        this.chatCount = chatCount;
        this.articleCount = articleCount;
        this.likeCount = likeCount;
        this.localDateTime = localDateTime;
    }
}
