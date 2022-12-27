package com.carrot.presentation.response;


import com.carrot.application.region.dto.RegionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionSearchResponse {
    private Long id;
    private String name;

    public static RegionSearchResponse fromRegionDto(RegionDto regionDto) {
        return RegionSearchResponse.builder()
                .id(regionDto.getId())
                .name(regionDto.getName())
                .build();
    }
}
