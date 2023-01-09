package com.carrot.testutil.fixture;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.application.article.domain.Sentence;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;

import static com.carrot.presentation.request.ArticleDto.ArticleSaveRequest;
import static com.carrot.presentation.request.ArticleDto.ArticleUpdateRequest;
import static java.time.LocalDateTime.now;

public class ArticleFixture {

    private static final String DEFAULT_SENTENCE = "DEFAULT_SENTENCE";

    public static Article get(Long id, User user, Post post){
        return Article.builder()
                .id(id)
                .user(user)
                .post(post)
                .sentence(new Sentence(DEFAULT_SENTENCE))
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
}
