package com.carrot.presentation.response;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.domain.entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.presentation.response.RegionResponse.*;
import static com.carrot.presentation.response.UserResponse.*;

public class PostResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailResponse {
        private Long postId;
        private UserProfileResponse userResponse;
        private RegionBasicResponse regionResponse;
        private PostImageResponses imageResponse;
        private String title;
        private String content;
        private Integer price;
        private String statue;
        private String category;
        private Integer hitsCount;
        private Integer chatCount;
        private Integer articleCount;
        private Integer likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PostDetailResponse of(final Post post) {
            return PostDetailResponse.builder()
                    .postId(post.getId())
                    .userResponse(UserProfileResponse.of(post.getUser()))
                    .regionResponse(RegionBasicResponse.of(post.getRegion()))
                    .imageResponse(PostImageResponses.convert(post.getPostImages()))
                    .title(post.getContent().getTitle())
                    .content(post.getContent().getContent())
                    .price(post.getContent().getPrice())
                    .statue(post.getStatus().getState())
                    .category(post.getCategory().getName())
                    .hitsCount(post.getHits())
                    .chatCount(post.getChatNum())
                    .articleCount(post.getArticleNum())
                    .likeCount(post.getLikeNum())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostImageResponses{
        List<PostImageResponse> responses;

        public static PostImageResponses of(final List<PostImageResponse> responses){
            return PostImageResponses.builder()
                    .responses(responses)
                    .build();
        }
        public static PostImageResponses convert(final List<PostImage> images){
            List<PostImageResponse> imageResponses = images.stream()
                    .map(PostImageResponse::of)
                    .collect(Collectors.toList());
            return new PostImageResponses(imageResponses);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostImageResponse {
        private Long imageId;
        private String title;
        private String url;
        public static PostImageResponse of(final PostImage image) {
            return PostImageResponse.builder()
                    .imageId(image.getId())
                    .title(image.getOriginName())
                    .url(image.getImageUrl())
                    .build();
        }
    }

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

        public static PostListResponse of(final Post post) {
            return PostListResponse.builder()
                    .postId(post.getId())
                    .regionName(post.getRegion().getName())
                    .title(post.getContent().getTitle())
                    .price(post.getContent().getPrice())
                    .statue(post.getStatus().getState())
                    .category(post.getCategory().getName())
                    .thumbnail(post.getThumbnail())
                    .hitsCount(post.getHits())
                    .chatCount(post.getChatNum())
                    .articleCount(post.getArticleNum())
                    .likeCount(post.getLikeNum())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
    }
}
