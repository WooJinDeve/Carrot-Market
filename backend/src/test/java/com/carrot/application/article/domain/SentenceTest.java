package com.carrot.application.article.domain;

import com.carrot.global.error.CarrotRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.carrot.global.error.ErrorCode.ARTICLE_SENTENCE_LENGTH_ERROR;

@DisplayName("[Domain] Sentence")
class SentenceTest {

    @DisplayName("[Success] Sentence 생성 요청")
    @Test
    void givenSentence_whenConstruct_thenConstructedSentence() {
        //given
        String sentence = "a".repeat(255);

        Assertions.assertThatCode(() -> new Sentence(sentence))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 댓글 길이가 255자를 초과할 경우")
    @Test
    void givenSentence_whenConstruct_thenThrowOverLength() {
        //given
        String sentence = "a".repeat(256);

        Assertions.assertThatCode(() -> new Sentence(sentence))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(ARTICLE_SENTENCE_LENGTH_ERROR.getMessage());
    }

    @DisplayName("[Error] 댓글 내용의 길이가 없을 경우")
    @Test
    void givenSentence_whenConstruct_thenThrowLowOverLength() {
        //given
        String sentence = "";

        Assertions.assertThatCode(() -> new Sentence(sentence))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(ARTICLE_SENTENCE_LENGTH_ERROR.getMessage());
    }
}