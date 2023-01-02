package com.carrot.application.user.repository;

import com.carrot.application.user.domain.Email;
import com.carrot.application.user.domain.User;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.global.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static com.carrot.global.error.ErrorCode.USER_NOTFOUND_ERROR;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(Email email);
    default User getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(USER_NOTFOUND_ERROR));
    }

    @Query(value = "SELECT DISTINCT u from User u left join fetch u.userRegions")
    Optional<User> findByIdWithUserRegion(Long userId);

    default User getByIdWithUserRegion(Long userId){
        return findByIdWithUserRegion(userId)
                .orElseThrow(() -> new CarrotRuntimeException(ErrorCode.USER_NOTFOUND_ERROR));
    }
}
