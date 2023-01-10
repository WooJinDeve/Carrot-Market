package com.carrot.application.article.repository;

import com.carrot.application.article.domain.Article;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.ARTICLE_NOTFOUND_ERROR;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "SELECT a FROM Article a JOIN FETCH a.user WHERE a.post.id = :postId ")
    Slice<Article> findAllByPostIdOrderByIdDesc(Long postId, Pageable pageable);

    @Query("SELECT a FROM Article a JOIN FETCH a.post WHERE a.id = :id")
    Optional<Article> findByIdWithPost(@Param("id") Long id);

    default Article getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(ARTICLE_NOTFOUND_ERROR));
    }

    default Article getByIdWithPost(Long id){
        return findByIdWithPost(id)
                .orElseThrow(() -> new CarrotRuntimeException(ARTICLE_NOTFOUND_ERROR));
    }
}
