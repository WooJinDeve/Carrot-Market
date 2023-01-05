package com.carrot.application.article.repository;

import com.carrot.application.article.domain.Article;
import com.carrot.application.article.domain.Reply;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.ARTICLE_NOTFOUND_ERROR;

public interface ReplyRepository extends JpaRepository<Reply, Long> {


    @Query("SELECT r FROM Reply r JOIN FETCH r.article WHERE r.id = :id")
    Optional<Reply> findByIdWithArticle(final @Param("id") Long id);

    boolean existsByArticle(final Article article);

    default Reply getByIdWithArticle(final Long id){
        return findByIdWithArticle(id)
                .orElseThrow(() -> new CarrotRuntimeException(ARTICLE_NOTFOUND_ERROR));
    }
    default Reply getById(final Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(ARTICLE_NOTFOUND_ERROR));
    }
}
