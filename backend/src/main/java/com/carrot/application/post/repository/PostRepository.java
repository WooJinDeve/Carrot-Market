package com.carrot.application.post.repository;

import com.carrot.application.post.domain.entity.Post;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.carrot.global.error.ErrorCode.*;

public interface PostRepository extends JpaRepository<Post, Long> {

    default Post getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(POST_NOTFOUND_ERROR));
    }
}
