package com.carrot.application.post.repository;

import com.carrot.application.post.domain.entity.BookedHistory;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.BOOKED_NOTFOUND_ERROR;

public interface BookedHistoryRepository extends JpaRepository<BookedHistory, Long> {

    @Query("select bh from BookedHistory bh join fetch bh.post where bh.post.id = :postId")
    Optional<BookedHistory> findByPostIdWithPost(@Param("postId") Long postId);

    default BookedHistory getByPostIdWithPost(Long postId){
        return findByPostIdWithPost(postId)
                .orElseThrow(() -> new CarrotRuntimeException(BOOKED_NOTFOUND_ERROR));
    }
}
