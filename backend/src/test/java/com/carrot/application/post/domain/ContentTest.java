package com.carrot.application.post.domain;

import com.carrot.global.error.CarrotRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.carrot.global.error.ErrorCode.POST_TITLE_VALIDATION_ERROR;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("[Domain] Content")
class ContentTest {

    @DisplayName("[Success] Content 생성 요청")
    @Test
    void Content_생성_요청() {
        //given
        String title = "title";
        String content = "content";
        Integer price = 100;


        //when & then
        assertThatCode(() -> Content.builder()
                .title(title)
                .content(content)
                .price(price)
                .build())
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 제목이 50자를 초과할 경우 예외발생")
    @Test
    void 제목이_50자를_초과할_경우_예외발생() throws Exception {
        //given
        String title = "title".repeat(11);
        String content = "content";
        Integer price = 100;

        //when & then
        assertThatCode(() -> Content.builder()
                .title(title)
                .content(content)
                .price((price))
                .build())
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(POST_TITLE_VALIDATION_ERROR.getMessage());
    }
}