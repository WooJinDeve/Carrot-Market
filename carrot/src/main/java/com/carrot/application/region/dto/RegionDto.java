package com.carrot.application.region.dto;

import com.carrot.application.region.domain.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {

    private Long id;
    private String code;
    private String name;
    private double x;
    private double y;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RegionDto fromRegion(Region region) {
        return RegionDto.builder()
                .id(region.getId())
                .code(region.getRegionCode())
                .name(region.getName())
                .x(region.getLocation().getX())
                .y(region.getLocation().getY())
                .createdAt(region.getCreatedAt())
                .updatedAt(region.getUpdatedAt())
                .build();
    }
}
