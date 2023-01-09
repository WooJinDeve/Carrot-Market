package com.carrot.application.article.service;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.application.article.repository.ArticleRepository;
import com.carrot.application.article.repository.ReplyRepository;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.application.user.service.UserValidator;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.testutil.ServiceTest;
import com.carrot.testutil.fixture.ArticleFixture;
import com.carrot.testutil.fixture.PostFixture;
import com.carrot.testutil.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.carrot.global.error.ErrorCode.*;
import static com.carrot.presentation.request.ArticleDto.ArticleSaveRequest;
import static com.carrot.presentation.request.ArticleDto.ArticleUpdateRequest;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("[Business] ArticleWriteServiceTest")
class ArticleWriteServiceTest extends ServiceTest {

    @InjectMocks
    private ArticleWriteService articleWriteService;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PostRepository postRepository;


    @DisplayName("[Success] 댓글 등록 요청 성공")
    @Test
    void givenArticleSaveRequest_whenSaving_thenSaveArticle() {
        //given
        ArticleSaveRequest request = ArticleFixture.getSaveRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);
        when(articleRepository.save(any())).thenReturn(mock(Article.class));

        //then
        assertThatCode(() -> articleWriteService.saveArticle(userFixture.getId(), postFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 댓글 등록 요청시, 삭제된 게시물인 경우")
    @Test
    void givenArticleSaveRequest_whenSaving_thenThrowDeletedPost() {
        //given
        ArticleSaveRequest request = ArticleFixture.getSaveRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, now());

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(postRepository.getById(any())).thenReturn(postFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.saveArticle(userFixture.getId(), postFixture.getId(), request));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 대댓글 등록 요청 성공")
    @Test
    void givenArticleSaveRequest_whenSaving_thenSaveReply() {
        //given
        ArticleSaveRequest request = ArticleFixture.getSaveRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L);
        Article articleFixture = ArticleFixture.get(1L, userFixture, postFixture);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);
        when(replyRepository.save(any())).thenReturn(mock(Reply.class));

        //then
        assertThatCode(() -> articleWriteService.saveReply(userFixture.getId(), articleFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 대댓글 등록 요청시, 삭제된 게시물인 경우")
    @Test
    void givenArticleSaveRequest_whenSaving_thenThrowDeletedPostReply() {
        //given
        ArticleSaveRequest request = ArticleFixture.getSaveRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.get(1L, userFixture, PostFixture.get(1L, userFixture, now()));

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.saveReply(userFixture.getId(), articleFixture.getId(), request));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 댓글 수정 요청")
    @Test
    void givenArticleUpdateRequest_whenUpdating_thenUpdateArticle() {
        //given
        ArticleUpdateRequest request = ArticleFixture.getUpdateRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.get(1L, userFixture, PostFixture.get(1L));

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);

        //then
        assertThatCode(() -> articleWriteService.updateArticle(userFixture.getId(), articleFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Error] 댓글 수정 요청시, 삭제된 게시물인 경우")
    @Test
    void givenArticleUpdateRequest_whenUpdating_thenThrowDeletedPost() {
        //given
        ArticleUpdateRequest request = ArticleFixture.getUpdateRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.get(1L, userFixture, PostFixture.get(1L, userFixture, now()));

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.updateArticle(userFixture.getId(), articleFixture.getId(), request));
        assertThat(POST_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 댓글 수정 요청시, 작성자와 요청자가 다른 경우")
    @Test
    void givenArticleUpdateRequest_whenUpdating_thenThrowDifferentUser() {
        //given
        ArticleUpdateRequest request = ArticleFixture.getUpdateRequest("sentence");

        User requestUserFixture = UserFixture.get(1L);
        User writeUserFixture = UserFixture.get(2L);
        Article articleFixture = ArticleFixture.get(1L, writeUserFixture, PostFixture.get(1L));

        //when
        when(userRepository.getById(any())).thenReturn(requestUserFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.updateArticle(requestUserFixture.getId(), articleFixture.getId(), request));
        assertThat(ARTICLE_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Error] 댓글 수정 요청시, 삭제된 댓글인 경우")
    @Test
    void givenArticleUpdateRequest_whenUpdating_thenThrowDeletedArticle() {
        //given
        ArticleUpdateRequest request = ArticleFixture.getUpdateRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.getDeleted(1L, userFixture, PostFixture.get(1L));

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.updateArticle(userFixture.getId(), articleFixture.getId(), request));
        assertThat(ARTICLE_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 대댓글 수정 요청")
    @Test
    void givenArticleUpdateRequest_whenUpdating_thenUpdateReply() {
        //given
        ArticleUpdateRequest request = ArticleFixture.getUpdateRequest("sentence");

        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.get(1L, userFixture, PostFixture.get(1L));
        Reply replyFixture = ArticleFixture.get(1L, userFixture, articleFixture);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(replyRepository.getById(any())).thenReturn(replyFixture);

        //then
        assertThatCode(() -> articleWriteService.updateReply(userFixture.getId(), articleFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 대댓글 수정 요청시, 작성자와 요청자가 다른 경우")
    @Test
    void givenArticleUpdateRequest_whenUpdating_thenThrowDifferentUserReply() {
        //given
        ArticleUpdateRequest request = ArticleFixture.getUpdateRequest("sentence");

        User requestUserFixture = UserFixture.get(1L);
        User writeUserFixture = UserFixture.get(2L);
        Article articleFixture = ArticleFixture.get(1L, writeUserFixture, PostFixture.get(1L));
        Reply replyFixture = ArticleFixture.get(1L, writeUserFixture, articleFixture);

        //when
        when(userRepository.getById(any())).thenReturn(requestUserFixture);
        when(replyRepository.getById(any())).thenReturn(replyFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.updateReply(requestUserFixture.getId(), replyFixture.getId(), request));
        assertThat(ARTICLE_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 댓글 삭제 요청")
    @Test
    void givenArticleId_whenDeleting_thenDeleteArticle() {
        //given
        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.get(1L, userFixture, PostFixture.get(1L));

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);

        //then
        assertThatCode(() -> articleWriteService.deleteArticle(userFixture.getId(), articleFixture.getId()))
                .doesNotThrowAnyException();
    }


    @DisplayName("[Success] 댓글 삭제 요청시, 이미 삭제처리된 댓글인 경우")
    @Test
    void givenArticleId_whenDeleting_thenThrowDeletedArticle() {
        //given
        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.getDeleted(1L, userFixture, PostFixture.get(1L));

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);


        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.deleteArticle(userFixture.getId(), articleFixture.getId()));
        assertThat(ARTICLE_NOTFOUND_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 댓글 삭제 요청시, 작성자와 요청자가 다른경우")
    @Test
    void givenArticleId_whenDeleting_thenThrowDifferentUser() {
        //given
        User requestUserFixture = UserFixture.get(1L);
        User writeUserFixture = UserFixture.get(2L);
        Article articleFixture = ArticleFixture.get(1L, writeUserFixture, PostFixture.get(1L));

        //when
        when(userRepository.getById(any())).thenReturn(requestUserFixture);
        when(articleRepository.getByIdWithPost(any())).thenReturn(articleFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.deleteArticle(requestUserFixture.getId(), articleFixture.getId()));
        assertThat(ARTICLE_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }

    @DisplayName("[Success] 대댓글 삭제 요청")
    @Test
    void givenReplyId_whenDeleting_thenDeleteReply() {
        //given
        User userFixture = UserFixture.get(1L);
        Article articleFixture = ArticleFixture.get(1L, userFixture, PostFixture.get(1L));
        Reply replyFixture = ArticleFixture.get(1L, userFixture, articleFixture);

        //when
        when(userRepository.getById(any())).thenReturn(userFixture);
        when(replyRepository.getByIdWithArticle(any())).thenReturn(replyFixture);
        doNothing().when(replyRepository).delete(any());

        //then
        assertThatCode(() -> articleWriteService.deleteReply(userFixture.getId(), articleFixture.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 대댓글 삭제 요청시, 작성자와 요청자가 다른 경우")
    @Test
    void givenReplyId_whenDeleting_thenThrowDifferentUser() {
        //given
        User requestUserFixture = UserFixture.get(1L);
        User writeUserFixture = UserFixture.get(2L);
        Article articleFixture = ArticleFixture.get(1L, requestUserFixture, PostFixture.get(1L));
        Reply replyFixture = ArticleFixture.get(1L, writeUserFixture, articleFixture);

        //when
        when(userRepository.getById(any())).thenReturn(requestUserFixture);
        when(replyRepository.getByIdWithArticle(any())).thenReturn(replyFixture);

        //then
        CarrotRuntimeException e = assertThrows(CarrotRuntimeException.class,
                () -> articleWriteService.deleteReply(requestUserFixture.getId(), articleFixture.getId()));
        assertThat(ARTICLE_VALIDATION_ERROR).isEqualTo(e.getErrorCode());
    }
}