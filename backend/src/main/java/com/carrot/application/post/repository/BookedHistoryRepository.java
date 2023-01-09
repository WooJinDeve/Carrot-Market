package com.carrot.application.post.repository;

import com.carrot.application.post.domain.entity.BookedHistory;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.BOOKED_NOTFOUND_ERROR;

public interface BookedHistoryRepository extends JpaRepository<BookedHistory, Long> {

    Optional<BookedHistory> findByPostId(Long postId);

    default BookedHistory getByPostId(Long postId){
        return findByPostId(postId)
                .orElseThrow(() -> new CarrotRuntimeException(BOOKED_NOTFOUND_ERROR));
    }
}
