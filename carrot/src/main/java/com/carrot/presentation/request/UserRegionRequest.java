package com.carrot.presentation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegionRequest {

    private Long regionId;

    public UserRegionRequest(Long regionId) {
        this.regionId = regionId;
    }
}
