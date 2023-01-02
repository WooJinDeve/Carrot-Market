package com.carrot.application.post.domain;

import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.testutil.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.carrot.global.error.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("[Domain] Post")
class PostTest {

    @DisplayName("[Success] 조회수 증가 요청")
    @Test
    void 조회수_증가_요청() {
        //given
        Post post = Post.builder()
                .hits(0)
                .build();
        int actual = 1;

        //when & then
        assertThatCode(() -> post.verifyOverflowHits(actual))
                .doesNotThrowAnyException();
        assertThat(post.getHits()).isEqualTo(actual);
    }


    @DisplayName("[Error] 조회수 증가시 Integer Overflow가 발생할 경우 예외")
    @Test
    void 조회수_증가시_IntegerOverflow가_발생한경우() {
        //given
        Post post = Post.builder()
                .hits(Integer.MAX_VALUE)
                .build();
        int addHits = 1;

        //when & then
        assertThatCode(() -> post.verifyOverflowHits(addHits))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(POST_HITS_OVERFLOW_ERROR.getMessage());
    }

    @DisplayName("[Success] 게시물 삭제 여부 검증 요청")
    @Test
    void 게시물_삭제_여부_검증_요청() {
        //given
        Post post = Post.builder()
                .build();

        //when & then
        assertThatCode(post::verifySoftDeleted)
                .doesNotThrowAnyException();
        assertThat(post.getDeletedAt()).isNull();
    }

    @DisplayName("[Error] 게시물 삭제 여부 검증시 SoftRemoved 경우 예외발생")
    @Test
    void 게시물_삭제_여부_검증시_SoftRemoved_경우() {
        //given
        Post post = Post.builder()
                .deletedAt(now())
                .build();

        //when & then
        assertThatCode(post::verifySoftDeleted)
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(POST_NOTFOUND_ERROR.getMessage());
    }

    @DisplayName("[Success] 게시물 작성자 검증 요청")
    @Test
    void 게시물_여부_검증_요청() {
        //given
        User fixture = UserFixture.get(1L);
        Post post = Post.builder()
                .user(fixture)
                .build();

        //when & then
        assertThatCode(() -> post.verifyOwner(fixture.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 게시물 작성자 검증시 타인일 경우 예외발생")
    @Test
    void 게시물_작성자_검증시_타인일_경우() {
        //given
        User writer = UserFixture.get(1L);
        User requester = UserFixture.get(2L);
        Post post = Post.builder()
                .user(writer)
                .build();

        //when & then
        assertThatCode(() -> post.verifyOwner(requester.getId()))
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(POST_VALIDATION_ERROR.getMessage());
    }
}