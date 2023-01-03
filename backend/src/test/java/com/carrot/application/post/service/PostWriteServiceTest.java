package com.carrot.application.post.service;

import com.carrot.application.post.domain.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.region.domain.Region;
import com.carrot.application.region.repository.RegionRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.application.user.service.UserValidator;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.presentation.request.PostRequest.PostSaveRequest;
import com.carrot.testutil.ServiceTest;
import com.carrot.testutil.fixture.PostFixture;
import com.carrot.testutil.fixture.RegionFixture;
import com.carrot.testutil.fixture.UserFixture;
import com.carrot.testutil.fixture.UserRegionFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.carrot.global.error.ErrorCode.*;
import static com.carrot.presentation.request.PostRequest.PostUpdateRequest;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@DisplayName("[Business] PostWriteService")
class PostWriteServiceTest extends ServiceTest {

    @InjectMocks
    private PostWriteService postWriteService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RegionRepository regionRepository;

    @Mock
    private UserValidator userValidator;

    @DisplayName("[Success] 게시물 생성 요청")
    @Test
    void 게시물_생성_요청() {
        //given
        PostSaveRequest request = PostFixture.getSaveRequest("title", "content");

        Region regionFixture = RegionFixture.get(1L);
        UserRegion userRegionFixture = UserRegionFixture.get(1L, null, regionFixture);
        User userFixture = UserFixture.getWithUserRegion(1L, userRegionFixture);

        //when
        when(userRepository.getByIdWithUserRegion(any())).thenReturn(userFixture);
        when(regionRepository.getById(any())).thenReturn(regionFixture);
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        //then
        assertThatCode(() -> postWriteService.createPost(any(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 사용자의 대표 지역이 존재하지 않는 경우")
    @Test
    void 대표_지역이_존재하지_경우() {
        //given
        PostSaveRequest request = PostFixture.getSaveRequest("title", "content");

        User userFixture = UserFixture.get(1L);

        //when
        when(userRepository.getByIdWithUserRegion(any())).thenReturn(userFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.createPost(userFixture.getId(), request));
        assertThat(USER_REGION_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }


    @DisplayName("[Error] 사용자의 대표 지역이 존재하지 않는 지역인 경우")
    @Test
    void 대표_지역이_존재하지_않는_지역인_경우() {
        //given
        PostSaveRequest request = PostFixture.getSaveRequest("title", "content");

        Region regionFixture = RegionFixture.get(1L);
        UserRegion userRegionFixture = UserRegionFixture.get(1L, null, regionFixture);
        User userFixture = UserFixture.getWithUserRegion(1L, userRegionFixture);

        //when
        when(userRepository.getByIdWithUserRegion(any())).thenReturn(userFixture);
        doThrow(new CarrotRuntimeException(REGION_NOTFOUND_ERROR))
                .when(regionRepository).getById(1L);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.createPost(userFixture.getId(), request));
        assertThat(REGION_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 게시물 수정 요청")
    @Test
    void 게시물_수정_요청() {
        //given
        PostUpdateRequest request = PostFixture.getUpdateRequest("title", "content");

        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);

        //then
        assertThatCode(() -> postWriteService.update(userFixture.getId(), postFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 게시물 수정 요청시, 게시물이 존재하지 않는 경우")
    @Test
    void 게시물_수정_요청시_게시물이_존재하지_않는_경우() {
        //given
        PostUpdateRequest request = PostFixture.getUpdateRequest("title", "content");

        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        doThrow(new CarrotRuntimeException(POST_NOTFOUND_ERROR)).when(postRepository).getById(any());

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.update(userFixture.getId(), postFixture.getId(), request));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 게시물 수정 요청시, 게시물이 삭제 상태인 경우")
    @Test
    void 게시물_수정_요청시_게시물이_삭제_상태인_경우() {
        //given
        PostUpdateRequest request = PostFixture.getUpdateRequest("title", "content");

        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, now());

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.update(userFixture.getId(), postFixture.getId(), request));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 게시물 수정 요청시, 타인이 수정 요청을 한 경우")
    @Test
    void 게시물_수정_요청시_타인이_수정_요청을_한_경우() {
        //given
        PostUpdateRequest request = PostFixture.getUpdateRequest("title", "content");

        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, UserFixture.get(2L), null);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.update(userFixture.getId(), postFixture.getId(), request));
        assertThat(POST_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 게시물 삭제 요청")
    @Test
    void 게시물_삭제_요청() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);

        //then
        assertThatCode(() -> postWriteService.delete(userFixture.getId(), postFixture.getId()))
                .doesNotThrowAnyException();
    }
}