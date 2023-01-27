package com.carrot.application.article.domain;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.support.fixture.ArticleFixture;
import com.carrot.support.fixture.PostFixture;
import com.carrot.support.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.carrot.global.error.ErrorCode.ARTICLE_NOTFOUND_ERROR;
import static com.carrot.global.error.ErrorCode.ARTICLE_VALIDATION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("[Domain] Sentence")
class ArticleTest {

    @DisplayName("[Error] 댓글의 작성자가 아닌경우 접근 권한 검증")
    @Test
    void givenArticle_whenValid_thenThrowNotMatchOwner() {
        //given
        Article article = ArticleFixture.get(1L, UserFixture.get(1L), PostFixture.get(1L));
        Long userId = 2L;

        //when & then
        assertThatCode(() -> article.verifyOwner(userId))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(ARTICLE_VALIDATION_ERROR.getMessage());
    }


    @DisplayName("[Success] 댓글의 내용 변경")
    @Test
    void givenArticle_whenChanging_thenChangedSentence() {
        //given
        Article article = ArticleFixture.get(1L, UserFixture.get(1L), PostFixture.get(1L));
        String expected = "수정 댓글";

        //when
        article.change(expected);

        //then
        assertThat(article.getSentence().getSentence()).isEqualTo(expected);
    }

    @DisplayName("[Success] 댓글이 삭제된 상태인지 검증")
    @Test
    void givenArticle_whenValid_thenValidatedStatus() {
        //given
        Article article = Article.builder()
                .id(1L)
                .user(UserFixture.get(1L))
                .post(PostFixture.get(1L))
                .build();

        //when
        article.softDeleted();

        //then
        assertThat(article.isDeleted()).isTrue();
    }

    @DisplayName("[Error] 댓글이 삭제된 상태인 경우 접근 권한 설정")
    @Test
    void givenArticle_whenValid_thenThrowValidatedSoftDeleted() {
        //given
        Article article = Article.builder()
                .id(1L)
                .user(UserFixture.get(1L))
                .post(PostFixture.get(1L))
                .deletedAt(LocalDateTime.now())
                .build();

        //when & then
        assertThatCode(article::verifySoftDeleted)
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(ARTICLE_NOTFOUND_ERROR.getMessage());

    }
}