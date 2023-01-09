package com.carrot.application.article.service;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.application.article.repository.ArticleRepository;
import com.carrot.application.article.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.presentation.response.ArticleResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleReadService {

    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, 20);

    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

    public ArticleResponses getArticles(Long postId, Pageable pageable){
        Slice<Article> articles = articleRepository.findAllByPostIdOrderByIdDesc(postId, pageable);

        return ArticleResponses.builder()
                .articles(convertToArticleItemResponses(articles.getContent()))
                .hasNext(articles.hasNext())
                .build();
    }

    private List<ArticleItemResponse> convertToArticleItemResponses(List<Article> articles){
        return articles.stream()
                .map(this::convertToArticleItemResponses)
                .collect(Collectors.toList());
    }

    private ArticleItemResponse convertToArticleItemResponses(Article article){
        if (article.isDeleted()){
            return ArticleItemResponse.softRemoveOf(article, getReplies(article.getId(), DEFAULT_PAGE_REQUEST));
        }
        return ArticleItemResponse.of(article, article.getUser(), getReplies(article.getId(), DEFAULT_PAGE_REQUEST));
    }

    public ReplyResponses getReplies(Long articleId, Pageable pageable){
        Slice<Reply> replies = replyRepository.findAllByArticleIdOrderByIdDesc(articleId, pageable);
        return ReplyResponses.builder()
                .replies(convertToReplyItemResponses(replies.getContent()))
                .hasNext(replies.hasNext())
                .build();
    }

    private List<ReplyItemResponse> convertToReplyItemResponses(List<Reply> replies){
        return replies.stream()
                .map(this::convertToReplyItemResponse)
                .collect(Collectors.toList());
    }

    private ReplyItemResponse convertToReplyItemResponse(Reply reply){
        return ReplyItemResponse.of(reply, reply.getUser());
    }
}
