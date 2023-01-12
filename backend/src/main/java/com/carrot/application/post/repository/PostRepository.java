package com.carrot.application.post.repository;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.*;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findByIdWithUser(Long id);

    default Post getByIdWithUser(Long id){
        return findByIdWithUser(id)
                .orElseThrow(() -> new CarrotRuntimeException(POST_NOTFOUND_ERROR));
    }

    default Post getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(POST_NOTFOUND_ERROR));
    }
}
