package com.carrot.application.region.repository;

import com.carrot.application.region.domain.Region;
import com.carrot.global.error.CarrotRuntimeException;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.carrot.global.error.ErrorCode.REGION_NOTFOUND_ERROR;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Slice<Region> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT r.id FROM Region r WHERE within(r.location, :filter) = true")
    List<Long> findRegionWithin(Geometry filter);

    default Region getById(Long id){
        return findById(id)
                .orElseThrow(() -> new CarrotRuntimeException(REGION_NOTFOUND_ERROR));
    }
}
