package com.carrot.application.like.service;

import com.carrot.application.like.domain.PostLike;
import com.carrot.application.like.event.PostLikeNotificationEvent;
import com.carrot.application.like.repository.PostLikeRepository;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.post.repository.PostRepository;
import com.carrot.application.user.domain.User;
import com.carrot.application.user.repository.UserRepository;
import com.carrot.application.user.service.UserValidator;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carrot.global.error.ErrorCode.POST_LIKE_NOTFOUND_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeWriteService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    private final ApplicationEventPublisher publisher;


    public void like(Long userId, Long postId){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Post post = postRepository.getById(postId);
        post.verifySoftDeleted();

        postLikeRepository.verifyExistByUserIdAndPostId(userId, postId);
        postLikeRepository.save(PostLike.of(post, user));

        publisher.publishEvent(new PostLikeNotificationEvent(userId, postId, post.getUser().getId()));
    }

    public void cancel(Long userId, Long postId){
        User user = userRepository.getById(userId);
        userValidator.validateDeleted(user);

        Post post = postRepository.getById(postId);
        post.verifySoftDeleted();

        PostLike postLike = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new CarrotRuntimeException(POST_LIKE_NOTFOUND_ERROR));
        postLikeRepository.delete(postLike);
    }
}
