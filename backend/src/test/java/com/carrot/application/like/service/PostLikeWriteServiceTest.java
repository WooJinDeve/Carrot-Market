package com.carrot.application.like.service;

import com.carrot.application.like.domain.PostLike;
import com.carrot.application.like.repository.PostLikeRepository;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.application.user.service.UserValidator;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.support.ServiceTest;
import com.carrot.support.fixture.PostFixture;
import com.carrot.support.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.POST_LIKE_NOTFOUND_ERROR;
import static com.carrot.global.error.ErrorCode.POST_LIKE_VALIDATION_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("[Business] PostLikeWriteService")
class PostLikeWriteServiceTest extends ServiceTest {

    @InjectMocks
    private PostLikeWriteService postLikeWriteService;
    @Mock
    private PostLikeRepository postLikeRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;

    @Spy
    private ApplicationEventPublisher publisher;

    @DisplayName("[Success] 게시물 좋아요 요청")
    @Test
    void 게시물_좋아요_요청() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        doNothing().when(postLikeRepository).verifyExistByUserIdAndPostId(userFixture.getId(), postFixture.getId());
        when(postLikeRepository.save(any())).thenReturn(mock(PostLike.class));

        //then
        Assertions.assertThatCode(() -> postLikeWriteService.like(userFixture.getId(), postFixture.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 게시물 좋아요 요청시, 이미 좋아요를 했을 경우")
    @Test
    void 게시물_좋아요_요청시_이미_좋아요를_했을_경우() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        doThrow(new CarrotRuntimeException(POST_LIKE_VALIDATION_ERROR))
                .when(postLikeRepository).verifyExistByUserIdAndPostId(userFixture.getId(), postFixture.getId());

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postLikeWriteService.like(userFixture.getId(), postFixture.getId()));
        assertThat(POST_LIKE_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 게시물 좋아요 취소 요청")
    @Test
    void 게시물_좋아요_취소_요청() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L);
        PostLike postLike = mock(PostLike.class);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        when(postLikeRepository.findByUserAndPost(userFixture, postFixture)).thenReturn(Optional.of(postLike));
        doNothing().when(postLikeRepository).delete(postLike);

        //then
        Assertions.assertThatCode(() -> postLikeWriteService.cancel(userFixture.getId(), postFixture.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 게시물 좋아요 취소 요청시, 저장된 좋아요 요청 기록이 없는 경우")
    @Test
    void 게시물_좋아요_취소_요청시_저장된_요청이_없는_경우() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        when(postLikeRepository.findByUserAndPost(userFixture, postFixture)).thenReturn(Optional.empty());

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postLikeWriteService.cancel(userFixture.getId(), postFixture.getId()));
        assertThat(POST_LIKE_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }
}