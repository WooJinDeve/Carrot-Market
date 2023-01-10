package com.carrot.application.like.repository;

import com.carrot.application.like.domain.PostLike;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.POST_LIKE_VALIDATION_ERROR;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    @Query("SELECT pl FROM PostLike pl JOIN FETCH pl.post WHERE pl.user.id = :userId")
    Slice<PostLike> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    default void verifyExistByUserIdAndPostId(Long userId, Long postId){
        if (existsByUserIdAndPostId(userId, postId))
            throw new CarrotRuntimeException(POST_LIKE_VALIDATION_ERROR);
    }
}
