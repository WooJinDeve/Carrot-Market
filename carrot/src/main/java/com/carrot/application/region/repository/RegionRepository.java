package com.carrot.application.region.repository;

import com.carrot.application.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByNameContaining(String name);
}
