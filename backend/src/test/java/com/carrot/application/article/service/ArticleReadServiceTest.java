package com.carrot.application.article.service;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.application.article.repository.ArticleRepository;
import com.carrot.application.article.repository.ReplyRepository;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;
import com.carrot.testutil.ServiceTest;
import com.carrot.testutil.fixture.ArticleFixture;
import com.carrot.testutil.fixture.PostFixture;
import com.carrot.testutil.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("[Business] ArticleReadService")
public class ArticleReadServiceTest extends ServiceTest {

    @InjectMocks
    private ArticleReadService articleReadService;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ReplyRepository replyRepository;

    @DisplayName("[Success] 댓글 조회 성공")
    @Test
    void givenPostId_whenFinding_thenFindArticleList() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);
        Article articleFixture = ArticleFixture.get(1L, userFixture, postFixture);
        PageRequest request = PageRequest.of(0, 20);
        SliceImpl<Article> fixture = new SliceImpl<>(List.of(articleFixture), request, false);

        //when
        when(articleRepository.findAllByPostIdOrderByIdDesc(any(), any())).thenReturn(fixture);
        when(replyRepository.findAllByArticleIdOrderByIdDesc(any(), any())).thenReturn(Page.empty());

        //then
        assertThatCode(() -> articleReadService.getArticles(postFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 댓글 조회시, 해당 게시물에 댓글이 없을 경우")
    @Test
    void givenPostId_whenFinding_thenEmptyArticleList() {
        //given
        User userFixture = UserFixture.get(1L);
        Post postFixture = PostFixture.get(1L, userFixture, null);
        PageRequest request = PageRequest.of(0, 20);

        //when
        when(articleRepository.findAllByPostIdOrderByIdDesc(any(), any())).thenReturn(Page.empty());

        //then
        assertThatCode(() -> articleReadService.getArticles(postFixture.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 대댓글 조회 성공")
    @Test
    void givenArticleId_whenFinding_thenFindReplies() {
        //given
        Long articleId = 1L;
        User userFixture = UserFixture.get(1L);
        Reply replyFixture = ArticleFixture.getReply(1L, userFixture);
        PageRequest request = PageRequest.of(0, 20);
        SliceImpl<Reply> fixture = new SliceImpl<>(List.of(replyFixture), request, false);

        //when
        when(replyRepository.findAllByArticleIdOrderByIdDesc(any(), any())).thenReturn(fixture);

        //then
        assertThatCode(() -> articleReadService.getReplies(articleId, request))
                .doesNotThrowAnyException();
    }

    @DisplayName("[Success] 대댓글 조회시, 해당 게시물에 대댓글이 없을 경우")
    @Test
    void givenArticleId_whenFinding_thenEmptyReplies() {
        //given
        Long articleId = 1L;
        PageRequest request = PageRequest.of(0, 20);

        //when
        when(replyRepository.findAllByArticleIdOrderByIdDesc(any(), any())).thenReturn(Page.empty());

        //then
        assertThatCode(() -> articleReadService.getReplies(articleId, request))
                .doesNotThrowAnyException();
    }
}

