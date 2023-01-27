package com.carrot.presentation.response;

import com.carrot.application.region.domain.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RegionResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionBasicResponse{
        private Long regionId;
        private String regionName;

        public static RegionBasicResponse of(Region region) {
            return RegionBasicResponse.builder()
                    .regionId(region.getId())
                    .regionName(region.getName())
                    .build();
        }
    }

}
