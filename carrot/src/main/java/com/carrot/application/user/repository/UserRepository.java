package com.carrot.application.user.repository;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(Email email);
}
