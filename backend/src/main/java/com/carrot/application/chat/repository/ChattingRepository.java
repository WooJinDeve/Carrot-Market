package com.carrot.application.chat.repository;

import com.carrot.application.chat.domain.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {
}
