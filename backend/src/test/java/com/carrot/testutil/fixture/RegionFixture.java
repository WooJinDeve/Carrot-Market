package com.carrot.testutil.fixture;

import com.carrot.application.region.domain.Region;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class RegionFixture {

    private static final String REGION_NAME = "region_name_fixture";
    private static final String REGION_CODE = "150001000";
    private static final Coordinate REGION_POINT = new Coordinate(36.5, 127.4);
    private static final Point REGION_LOCATION = new GeometryFactory().createPoint(REGION_POINT);

    public static Region get(Long id){
        return Region.builder()
                .id(id)
                .regionCode(REGION_CODE)
                .name(REGION_NAME)
                .location(REGION_LOCATION)
                .build();
    }

}
