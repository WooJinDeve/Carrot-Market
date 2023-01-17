package com.carrot.application.notification.repository;

import com.carrot.application.notification.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Slice<Notification> findAllByReceiverId(Long userId, Pageable pageable);
}
