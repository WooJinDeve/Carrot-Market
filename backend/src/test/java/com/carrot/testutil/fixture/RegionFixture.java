package com.carrot.testutil.fixture;

import com.carrot.application.region.domain.Region;

public class RegionFixture {

    private static final String REGION_NAME = "region_name_fixture";

    public static Region get(Long id){
        return Region.builder()
                .id(id)
                .name(REGION_NAME)
                .build();
    }

}
