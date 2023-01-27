package com.carrot.application.post.service;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.region.domain.Region;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.service.UserReadService;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.support.ServiceTest;
import com.carrot.support.fixture.PostFixture;
import com.carrot.support.fixture.RegionFixture;
import com.carrot.support.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.carrot.global.error.ErrorCode.POST_NOTFOUND_ERROR;
import static com.carrot.support.fixture.RegionFixture.get;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("[Business] PostReadService")
class PostReadServiceTest extends ServiceTest {

    @InjectMocks
    private PostReadService postReadService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserReadService userReadService;

    @DisplayName("[Success] 포스트 단건 조회 요청")
    @Test
    void givenPostId_whenFinding_thenFindPostDetail() {
        //given
        Long postId = 1L;

        Post postFixture = PostFixture.get(UserFixture.get(1L), get(1L));

        //when
        when(postRepository.getByIdByIdWithRegionAndUserAndImage(any())).thenReturn(postFixture);

        //then
        assertThatCode(() -> postReadService.findById(postId))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 포스트 단건 조회 요청시, 존재하지 않은 결우")
    @Test
    void givenPostId_whenFinding_thenThrowNotExistPost() {
        //given
        Long postId = 1L;

        //when
        doThrow(new CarrotRuntimeException(POST_NOTFOUND_ERROR))
                .when(postRepository).getByIdByIdWithRegionAndUserAndImage(any());
        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postReadService.findById(postId));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 포스트 단건 조회 요청시, 게시물이 삭제 상태일 경우")
    @Test
    void givenPostId_whenFinding_thenThrowValidSoftDeletedPost() {
        //given
        Long postId = 1L;
        Post postFixture = PostFixture.get(postId, null, now());

        //when
        when(postRepository.getByIdByIdWithRegionAndUserAndImage(any())).thenReturn(postFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postReadService.findById(postId));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 포스팅 전체 조회 요청")
    @Test
    void givenPostSearchRequest_whenFinding_thenPostListResponses() {
        //given
        User userFixture = UserFixture.get(1L);
        Region regionFixture = RegionFixture.get(1L);
        Post postFixture = PostFixture.get(userFixture, regionFixture);

        PageRequest request = PageRequest.of(0, 20);
        SliceImpl<Post> fixture = new SliceImpl<>(List.of(postFixture), request, false);
        List<Long> idsFixture = List.of(1L, 2L);

        //when
        when(userReadService.findSurroundAreaByUserRegion(any())).thenReturn(idsFixture);
        when(postRepository.findWithSearchConditions(any(), any(), any(), any())).thenReturn(fixture);

        //then
        assertThatCode(() -> postReadService.findBySearchConditions(userFixture.getId(), null, null, request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 포스팅 전체 조회시, 조회된 게시물이 없을 경우")
    @Test
    void givenPostSearchRequest_whenFinding_thenNotExistPostListResponses() {
        //given
        User userFixture = UserFixture.get(1L);

        PageRequest request = PageRequest.of(0, 20);
        List<Long> idsFixture = List.of(1L, 2L);

        //when
        when(userReadService.findSurroundAreaByUserRegion(any())).thenReturn(idsFixture);
        when(postRepository.findWithSearchConditions(any(), any(), any(), any())).thenReturn(Page.empty());

        //then
        assertThatCode(() -> postReadService.findBySearchConditions(userFixture.getId(), null, null, request))
                .doesNotThrowAnyException();
    }
}