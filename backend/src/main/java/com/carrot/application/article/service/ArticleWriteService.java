package com.carrot.application.article.service;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.application.article.repository.ArticleRepository;
import com.carrot.application.article.repository.ReplyRepository;
import com.carrot.application.post.domain.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.application.user.service.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carrot.presentation.request.ArticleDto.*;
import static com.carrot.presentation.request.ArticleDto.ArticleSaveRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleWriteService {

    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PostRepository postRepository;

    public void saveArticle(Long userId, Long postId, ArticleSaveRequest request){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Post post = postRepository.getById(postId);
        post.verifySoftDeleted();

        articleRepository.save(Article.of(user, post, request.getSentence()));
    }

    public void saveReply(Long userId, Long articleId, ArticleSaveRequest request){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Article article = articleRepository.getByIdWithPost(articleId);
        article.getPost().verifySoftDeleted();

        replyRepository.save(Reply.of(user, article, request.getSentence()));
    }

    public void updateArticle(Long userId, Long articleId, ArticleUpdateRequest request){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Article article = articleRepository.getByIdWithPost(articleId);
        article.getPost().verifySoftDeleted();

        article.verifyOwner(userId);
        article.verifySoftDeleted();
        article.change(request.getSentence());
    }

    public void updateReply(Long userId, Long replyId, ArticleUpdateRequest request){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Reply reply = replyRepository.getById(replyId);
        reply.verifyOwner(userId);

        reply.change(request.getSentence());
    }

    public void deleteArticle(Long userId, Long articleId){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Article article = articleRepository.getByIdWithPost(articleId);
        article.getPost().verifySoftDeleted();
        article.verifySoftDeleted();

        verifyWhenDeleteArticle(article);
    }

    public void deleteReply(Long userId, Long replyId){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Reply reply = replyRepository.getByIdWithArticle(replyId);
        reply.verifyOwner(userId);

        replyRepository.delete(reply);
        verifyWhenDeleteArticle(reply.getArticle());
    }

    private void verifyWhenDeleteArticle(Article article){
        if (replyRepository.existsByArticle(article)){
            articleRepository.delete(article);
            return;
        }
        article.softDeleted();
    }
}
