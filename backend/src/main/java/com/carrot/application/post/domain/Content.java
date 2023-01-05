package com.carrot.application.post.domain;

import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.infrastructure.util.ClassUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import static com.carrot.global.error.ErrorCode.POST_TITLE_VALIDATION_ERROR;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Content {

    private static final int MAX_TITLE_LENGTH = 50;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Lob
    private String content;


    @Builder
    public Content(String title, String content, Integer price) {
        ClassUtils.checkNotNullParameter(title, String.class);
        ClassUtils.checkNotNullParameter(content, String.class);
        ClassUtils.checkNotNullParameter(price, Integer.class);
        verifyOverLengthTitle(title);
        this.title = title;
        this.price = price;
        this.content = content;
    }




    private void verifyOverLengthTitle(String title){
        if (title.length() > 50)
            throw new CarrotRuntimeException(POST_TITLE_VALIDATION_ERROR);
    }
}
