package com.carrot.presentation.response;


import com.carrot.application.region.domain.Region;
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

    public static RegionSearchResponse of(Region region) {
        return RegionSearchResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .build();
    }
}
