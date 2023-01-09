package com.carrot.testutil.fixture;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.application.article.domain.Sentence;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.carrot.presentation.request.ArticleRequest.ArticleSaveRequest;
import static com.carrot.presentation.request.ArticleRequest.ArticleUpdateRequest;
import static com.carrot.presentation.response.ArticleResponse.*;
import static java.time.LocalDateTime.now;

public class ArticleFixture {

    private static final String DEFAULT_SENTENCE = "DEFAULT_SENTENCE";
    private static final String DEFAULT_NICKNAME = "DEFAULT_NICKNAME";

    public static Article get(Long id, User user, Post post){
        return Article.builder()
                .id(id)
                .user(user)
                .post(post)
                .sentence(new Sentence(DEFAULT_SENTENCE))
                .build();
    }

    public static Reply getReply(Long id, User user){
        return Reply.builder()
                .id(id)
                .sentence(new Sentence(DEFAULT_SENTENCE))
                .user(user)
                .build();
    }

    public static Article getDeleted(Long id, User user, Post post){
        return Article.builder()
                .id(id)
                .user(user)
                .post(post)
                .sentence(new Sentence(DEFAULT_SENTENCE))
                .deletedAt(now())
                .build();
    }

    public static Reply get(Long id, User user, Article article){
        return Reply.builder()
                .id(id)
                .user(user)
                .article(article)
                .sentence(new Sentence(DEFAULT_SENTENCE))
                .build();
    }


    public static ArticleSaveRequest getSaveRequest(String sentence){
        return ArticleSaveRequest
                .builder()
                .sentence(sentence)
                .build();
    }

    public static ArticleUpdateRequest getUpdateRequest(String sentence){
        return ArticleUpdateRequest
                .builder()
                .sentence(sentence)
                .build();
    }

    public static ArticleResponses getArticleResponses(long size, boolean hasNext){
        return ArticleResponses.builder()
                .articles(getArticleItemResponses(size))
                .hasNext(hasNext)
                .build();
    }

    private static List<ArticleItemResponse> getArticleItemResponses(long size){
        return LongStream.range(1, size + 1)
                .mapToObj(i -> getArticleItemResponse(i, i))
                .collect(Collectors.toList());
    }


    public static ArticleItemResponse getArticleItemResponse(Long articleId, Long userId){
        return ArticleItemResponse.builder()
                .articleId(articleId)
                .userId(userId)
                .nickname(DEFAULT_NICKNAME)
                .sentence(DEFAULT_SENTENCE)
                .replies(getReplyResponses(20, true))
                .build();
    }

    public static ReplyResponses getReplyResponses(long size, boolean hasNext){
        return ReplyResponses.builder()
                .replies(getReplyItemResponses(size))
                .hasNext(hasNext)
                .build();
    }

    private static List<ReplyItemResponse> getReplyItemResponses(long size){
        return LongStream.range(1, size + 1)
                .mapToObj(i -> getReplyItemResponse(i, i))
                .collect(Collectors.toList());
    }

    public static ReplyItemResponse getReplyItemResponse(Long replyId, Long userId){
        return ReplyItemResponse.builder()
                .replyId(replyId)
                .userId(userId)
                .nickname(DEFAULT_NICKNAME)
                .sentence(DEFAULT_SENTENCE)
                .createdAt(now())
                .updateAt(now())
                .build();
    }

}
