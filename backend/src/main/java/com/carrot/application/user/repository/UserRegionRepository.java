package com.carrot.application.user.repository;

import com.carrot.application.user.domain.UserRegion;
import com.carrot.global.error.CarrotRuntimeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.carrot.global.error.ErrorCode.USER_REGION_NOTFOUND_ERROR;

public interface UserRegionRepository extends JpaRepository<UserRegion, Long> {

    @Query("SELECT u FROM UserRegion u JOIN FETCH u.region WHERE u.user.id = :id")
    List<UserRegion> findByIdAndFetchRegion(@Param("id") Long userId);

    boolean existsByUserIdAndRegionId(Long userId, Long regionId);

    long countByUserId(Long userId);

    default UserRegion getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(USER_REGION_NOTFOUND_ERROR));
    }

    @Modifying
    @Query("UPDATE UserRegion u SET u.represent = true WHERE u.user.id = :userId")
    void changeNewRepresent(Long userId);
}
