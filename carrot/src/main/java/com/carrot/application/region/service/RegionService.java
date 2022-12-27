package com.carrot.application.region.service;

import com.carrot.application.region.dto.RegionDto;
import com.carrot.application.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionDto> search(String state) {
        return regionRepository.findByNameContaining(state).stream()
                .map(RegionDto::fromRegion)
                .collect(Collectors.toList());
    }
}
