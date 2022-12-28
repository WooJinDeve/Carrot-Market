package com.carrot.application.region.repository;

import com.carrot.application.region.domain.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Slice<Region> findByNameContaining(String name, Pageable pageable);
}
