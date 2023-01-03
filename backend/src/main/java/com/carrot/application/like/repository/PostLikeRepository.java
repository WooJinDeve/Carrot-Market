package com.carrot.application.like.repository;

import com.carrot.application.like.domain.PostLike;
import com.carrot.application.post.domain.Post;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.POST_LIKE_VALIDATION_ERROR;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    default void verifyExistByUserIdAndPostId(Long userId, Long postId){
        if (existsByUserIdAndPostId(userId, postId))
            throw new CarrotRuntimeException(POST_LIKE_VALIDATION_ERROR);
    }
}
