package com.carrot.application.post.domain;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.support.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.carrot.application.post.domain.PostStatue.*;
import static com.carrot.global.error.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("[Domain] Post")
class PostTest {

    @DisplayName("[Success] 조회수 증가 요청")
    @Test
    void givenPost_whenIncreasing_thenAddHits() {
        //given
        Post post = Post.builder()
                .build();
        int actual = 1;

        //when & then
        assertThatCode(() -> post.verifyOverflowHits(actual))
                .doesNotThrowAnyException();
        assertThat(post.getHits()).isEqualTo(actual);
    }


    @DisplayName("[Error] 조회수 증가시 Integer Overflow가 발생할 경우 예외")
    @Test
    void givenPost_whenIncreasing_thenThrowIntegerOverflow() {
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
    void givenPost_whenDeleting_thenVerifyDeletedPost() {
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
    void givenPost_whenDeleting_thenThrowSoftDeleted() {
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
    void givenPost_when_thenVerifyPostOwner() {
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
    void givenPost_when_thenThrowNotMatchPostOwner() {
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

    @DisplayName("[Success] 게시물 상태 예약 변경 요청")
    @Test
    void givenPost_whenChanging_thenChangeStatusBooked() {
        //given
        Post post = Post.builder()
                .status(SALE)
                .build();

        //when
        post.verifyAndStatueChangeBooked();

        //then
        assertThat(post.getStatus()).isEqualTo(BOOKED);
    }

    @DisplayName("[Error] 게시물의 상태가 Sale 아닐 경우, 예약상태로 변경시 예외발생")
    @Test
    void givenPost_whenChanging_thenThrowPostStatusIsNotSale() {
        //given
        Post post = Post.builder()
                .status(BOOKED)
                .build();

        //when & then
        assertThatCode(post::verifyAndStatueChangeBooked)
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(POST_VALIDATION_ERROR.getMessage());
    }

    @DisplayName("[Success] 게시물 상태 판매중 변경 요청")
    @Test
    void givenPost_whenChanging_thenChangeStatusSale() {
        //given
        Post post = Post.builder()
                .status(BOOKED)
                .build();

        //when
        post.verifyAndStatueChangeSale();

        //then
        assertThat(post.getStatus()).isEqualTo(SALE);
    }

    @DisplayName("[Error] 판매중 상태로 변경시, 게시물 상태가 예약이 아닐경우")
    @Test
    void givenPost_whenChanging_thenThrowPostStatusIsNotBooked() {
        //given
        Post post = Post.builder()
                .status(SALE)
                .build();

        //when & then
        assertThatCode(post::verifyAndStatueChangeSale)
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(POST_VALIDATION_ERROR.getMessage());
    }

    @DisplayName("[Success] 게시물 상태 판매완료 변경 요청")
    @Test
    void givenPost_whenChanging_thenChangeStatusSoldOut() {
        //given
        Post post = Post.builder()
                .status(BOOKED)
                .build();

        //when
        post.verifyAndStatueChangeSoldOut();

        //then
        assertThat(post.getStatus()).isEqualTo(SOLD_OUT);
    }

    @DisplayName("[Error] 판매중 상태로 변경시, 게시물 상태가 SOLD_OUT 또는 SALE 일 경우 예외발생")
    @Test
    void givenPost_whenChanging_thenThrowPostStatusIsNotMatch() {
        //given
        Post post = Post.builder()
                .status(SALE)
                .build();

        //when & then
        assertThatCode(post::verifyAndStatueChangeSoldOut)
                .isInstanceOf(CarrotRuntimeException.class)
                .hasMessage(POST_VALIDATION_ERROR.getMessage());
    }


}