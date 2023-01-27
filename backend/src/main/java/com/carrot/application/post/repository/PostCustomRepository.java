package com.carrot.application.post.repository;

import com.carrot.application.post.domain.Category;
import com.carrot.application.post.domain.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostCustomRepository {

    Slice<Post> findWithSearchConditions(List<Long> regionIds, Category category, String title, Pageable pageable);
}
