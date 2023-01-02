package com.carrot.application.post.domain;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.global.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import static com.carrot.global.error.ErrorCode.*;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Content {

    private static final int MAX_TITLE_LENGTH = 50;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Lob
    private String content;

    @Builder
    public Content(String title, String content) {
        verifyOverLengthTitle(title);
        this.title = title;
        this.content = content;
    }

    private void verifyOverLengthTitle(String title){
        if (title.length() > 50)
            throw new CarrotRuntimeException(POST_TITLE_VALIDATION_ERROR);
    }
}
