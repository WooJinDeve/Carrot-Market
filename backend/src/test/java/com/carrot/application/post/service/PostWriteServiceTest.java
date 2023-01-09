package com.carrot.application.post.service;

import com.carrot.application.post.domain.entity.BookedHistory;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.repository.BookedHistoryRepository;
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
import org.mockito.Spy;

import static com.carrot.application.post.domain.PostStatue.BOOKED;
import static com.carrot.application.post.domain.PostStatue.SALE;
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
    private BookedHistoryRepository bookedHistoryRepository;
    @Mock
    private RegionRepository regionRepository;

    @Spy
    private UserValidator userValidator;

    @DisplayName("[Success] 게시물 생성 요청")
    @Test
    void givenPostSaveRequest_whenSaving_thenSavePost() {
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
    void givenPostSaveRequest_whenSaving_thenThrowNotExistUserRegion() {
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
    void givenPostSaveRequest_whenSaving_thenThrowNotExistRepresentUserRegion() {
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
    void givenPostUpdateRequest_whenUpdating_thenUpdatePost() {
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
    void givenPostUpdateRequest_whenUpdating_thenThrowNotExistPost() {
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
    void givenPostUpdateRequest_whenUpdating_thenThrowStatusIsDeleted() {
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
    void givenPostUpdateRequest_whenUpdating_thenThrowNotMatchOwner() {
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
    void givenPostId_whenDeleting_thenDeletePost() {
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

    @DisplayName("[Success] 게시물 예약 요청")
    @Test
    void givenBookerId_whenBooked_thenBookedPost(){
        //given
        User sellerFixture = UserFixture.get(1L);
        User bookerFixture = UserFixture.get(2L);
        Post postFixture = PostFixture.get(1L, sellerFixture, null);

        //when
        when(userRepository.getById(sellerFixture.getId())).thenReturn(sellerFixture);
        when(userRepository.getById(bookerFixture.getId())).thenReturn(bookerFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        when(bookedHistoryRepository.save(any())).thenReturn(mock(BookedHistory.class));

        //then
        assertThatCode(() -> postWriteService.booked(sellerFixture.getId(), postFixture.getId(), bookerFixture.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 게시물 예약 요청시, 게시물의 상태가 판매중이 아닐 경우 예외 발생")
    @Test
    void givenBookerId_whenBooked_then() {
        //given
        User sellerFixture = UserFixture.get(1L);
        User bookerFixture = UserFixture.get(2L);
        Post postFixture = PostFixture.getStatue(1L, sellerFixture, BOOKED);

        //when
        when(userRepository.getById(sellerFixture.getId())).thenReturn(sellerFixture);
        when(userRepository.getById(bookerFixture.getId())).thenReturn(bookerFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.booked(sellerFixture.getId(), postFixture.getId(), bookerFixture.getId()));
        assertThat(POST_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 게시물 예약 취소 요청")
    @Test
    void givenPostId_WhenDeletingBooked_thenDeleteBookedAndChangePostStatue() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.getStatue(1L, userFixture, BOOKED);
        BookedHistory bookedFixture = mock(BookedHistory.class);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        when(bookedHistoryRepository.getByPostId(any())).thenReturn(bookedFixture);
        doNothing().when(bookedHistoryRepository).delete(bookedFixture);

        //then
        assertThatCode(() -> postWriteService.cancelBooked(userFixture.getId(), postFixture.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 게시물 예약 취소시, 예약된 정보가 저장되어 있지 않은경우 예외발생")
    @Test
    void givenPostId_WhenDeletingBooked_thenThrowNotExistBookedHistory() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.getStatue(1L, userFixture, BOOKED);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        doThrow(new CarrotRuntimeException(BOOKED_NOTFOUND_ERROR))
                .when(bookedHistoryRepository).getByPostId(any());

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.cancelBooked(userFixture.getId(), postFixture.getId()));
        assertThat(BOOKED_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 게시물 예약 취소시, 게시물의 상태가 예약된 상태가 아닌경우")
    @Test
    void givenPostId_WhenDeletingBooked_thenThrowPostStatusIsNotBooked() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.getStatue(1L, userFixture, SALE);
        BookedHistory bookedFixture = mock(BookedHistory.class);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        when(bookedHistoryRepository.getByPostId(any())).thenReturn(bookedFixture);


        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> postWriteService.cancelBooked(userFixture.getId(), postFixture.getId()));
        assertThat(POST_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }
}