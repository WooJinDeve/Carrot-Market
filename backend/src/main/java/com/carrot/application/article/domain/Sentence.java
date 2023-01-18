package com.carrot.application.article.domain;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.infrastructure.util.ClassUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.carrot.global.error.ErrorCode.ARTICLE_SENTENCE_LENGTH_ERROR;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Sentence {

    private static final int MAX_SENTENCE_LENGTH = 255;
    @Column(name = "sentence", nullable = false, length = 255)
    private String sentence;

    public Sentence(String sentence) {
        ClassUtils.checkNotNullParameter(sentence, String.class);
        verifyCharacterLength(sentence);
        this.sentence = sentence;
    }

    public void verifyCharacterLength(String sentence){
        if (sentence.length() == 0 || sentence.length() > MAX_SENTENCE_LENGTH){
            throw new CarrotRuntimeException(ARTICLE_SENTENCE_LENGTH_ERROR);
        }
    }

}
