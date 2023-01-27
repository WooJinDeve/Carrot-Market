package com.carrot.application.post.repository;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.*;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    @Query("SELECT p FROM Post p " +
            "JOIN FETCH p.region " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.postImages " +
            "WHERE p.id = :id")
    Optional<Post> findByIdWithRegionAndUserAndImage(Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findByIdWithUser(Long id);

    default Post getByIdByIdWithRegionAndUserAndImage(Long id){
        return findByIdWithRegionAndUserAndImage(id)
                .orElseThrow(() -> new CarrotRuntimeException(POST_NOTFOUND_ERROR));
    }
    default Post getByIdWithUser(Long id){
        return findByIdWithUser(id)
                .orElseThrow(() -> new CarrotRuntimeException(POST_NOTFOUND_ERROR));
    }

    default Post getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(POST_NOTFOUND_ERROR));
    }
}
