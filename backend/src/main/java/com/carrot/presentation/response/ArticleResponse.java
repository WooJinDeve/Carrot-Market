package com.carrot.presentation.response;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.application.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleResponses {
        private List<ArticleItemResponse> articles;
        private boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyResponses {
        private List<ReplyItemResponse> replies;
        private boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyItemResponse {
        private Long replyId;
        private Long userId;
        private String nickname;
        private String sentence;
        private LocalDateTime createdAt;
        private LocalDateTime updateAt;

        public static ReplyItemResponse of(final Reply reply, final User user) {
            return ReplyItemResponse.builder()
                    .replyId(reply.getId())
                    .userId(user.getId())
                    .nickname(user.getNickname().getNickname())
                    .sentence(reply.getSentence().getSentence())
                    .createdAt(reply.getCreatedAt())
                    .updateAt(reply.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleItemResponse {
        private static final String DELETED_NICKNAME = "unknown";
        private static final String DELETED_CONTENT = "작성자에 의해 삭제된 댓글입니다.";

        private Long articleId;
        private Long userId;
        private String nickname;
        private String sentence;
        private LocalDateTime createdAt;
        private LocalDateTime updateAt;
        private ReplyResponses replies;

        public static ArticleItemResponse of(final Article article,final User user, ReplyResponses replies){
            return ArticleItemResponse.builder()
                    .articleId(article.getId())
                    .userId(user.getId())
                    .nickname(user.getNickname().getNickname())
                    .sentence(article.getSentence().getSentence())
                    .replies(replies)
                    .build();
        }

        public static ArticleItemResponse softRemoveOf(final Article article, ReplyResponses replies){
            return ArticleItemResponse.builder()
                    .articleId(article.getId())
                    .nickname(DELETED_NICKNAME)
                    .sentence(DELETED_CONTENT)
                    .replies(replies)
                    .build();
        }
    }
}
