package com.carrot.application.region.service;

import com.carrot.application.region.domain.Region;
import com.carrot.application.region.repository.RegionRepository;
import com.carrot.infrastructure.util.CsvUtil;
import com.carrot.infrastructure.util.PointUtil;
import com.carrot.presentation.response.RegionSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private static final String CSV_LOCATION = "/csv/location.csv";
    private final RegionRepository regionRepository;

    public Slice<RegionSearchResponse> search(String state, Pageable pageable) {
        return regionRepository.findByNameContaining(state, pageable)
                .map(RegionSearchResponse::of);
    }

    @PostConstruct
    @Transactional
    public void insert(){
        if(regionRepository.count() == 0){
            List<Region> regions = convertToRegions();
            regionRepository.saveAll(regions);
        }
    }

    private List<Region> convertToRegions(){
        return Objects.requireNonNull(CsvUtil.readCsvLines(CSV_LOCATION))
                .stream()
                .map(this::convertToRegion)
                .collect(Collectors.toList());
    }

    private Region convertToRegion(String[] regions){
        return Region.of(regions[0], regions[1], PointUtil.createPoint(regions[2], regions[3]));
    }
}
