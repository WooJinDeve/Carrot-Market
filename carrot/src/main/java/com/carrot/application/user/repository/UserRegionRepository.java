package com.carrot.application.user.repository;

import com.carrot.application.user.domain.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRegionRepository extends JpaRepository<UserRegion, Long> {

    @Query("SELECT u FROM UserRegion u JOIN FETCH u.region WHERE u.user.id = :id")
    List<UserRegion> findByIdAndFetchRegion(@Param("id") Long userId);
    Optional<UserRegion> findByUserIdAndRegionId(Long userId, Long regionId);
    boolean existsByUserIdAndRegionId(Long userId, Long regionId);

    long countByUserId(Long userId);
}
