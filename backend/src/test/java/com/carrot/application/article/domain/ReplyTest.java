package com.carrot.application.article.domain;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.testutil.fixture.ArticleFixture;
import com.carrot.testutil.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.carrot.global.error.ErrorCode.ARTICLE_VALIDATION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("[Domain] Reply")
class ReplyTest {

    @DisplayName("[Error] 대댓글의 작성자가 아닌경우 접근 권한 검증")
    @Test
    void givenReply_whenValid_thenThrowNotMatchOwner() {
        //given
        Reply reply = ArticleFixture.getReply(1L, UserFixture.get(1L));
        Long userId = 2L;

        //when & then
        assertThatCode(() -> reply.verifyOwner(userId))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(ARTICLE_VALIDATION_ERROR.getMessage());
    }


    @DisplayName("[Success] 대댓글의 내용 변경")
    @Test
    void givenReply_whenChanging_thenChangedSentence() {
        //given
        Reply reply = ArticleFixture.getReply(1L, UserFixture.get(1L));
        String expected = "수정 댓글";

        //when
        reply.change(expected);

        //then
        assertThat(reply.getSentence().getSentence()).isEqualTo(expected);
    }
}