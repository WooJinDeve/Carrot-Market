package com.carrot.presentation.response;

import com.carrot.application.post.domain.entity.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionHistoryResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionHistoryListResponses {
        private List<TransactionHistoryListResponse> response;
        private boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionHistoryListResponse {
        private Long thId;
        private Long postId;
        private String title;
        private Integer price;
        private String statue;
        private String thumbnail;
        private Integer hitsCount;
        private Integer chatCount;
        private Integer articleCount;
        private Integer likeCount;
        private LocalDateTime createdAt;

        public static TransactionHistoryListResponse of(final TransactionHistory history) {
            return TransactionHistoryListResponse.builder()
                    .postId(history.getId())
                    .title(history.getPost().getContent().getTitle())
                    .price(history.getPost().getContent().getPrice())
                    .statue(history.getPost().getStatus().getState())
                    .thumbnail(history.getThumbnail())
                    .hitsCount(history.getPost().getHits())
                    .chatCount(history.getPost().getChatNum())
                    .articleCount(history.getPost().getArticleNum())
                    .likeCount(history.getPost().getLikeNum())
                    .createdAt(history.getCreatedAt())
                    .build();
        }
    }
}
