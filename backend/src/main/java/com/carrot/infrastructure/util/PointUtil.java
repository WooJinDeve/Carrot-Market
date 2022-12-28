package com.carrot.infrastructure.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;

public class PointUtil {

    public static final String EPSG_5179 = "EPSG:5179";
    public static final String EPSG_4326 = "EPSG:4326";

    private static ProjCoordinate getCastPoint(String x, String y){
        //parse to Double
        double dbLon = Double.parseDouble(x);
        double dbLat = Double.parseDouble(y);

        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem grs80 = factory.createFromName(EPSG_5179);
        CoordinateReferenceSystem wgs84 = factory.createFromName(EPSG_4326);

        ProjCoordinate before = new ProjCoordinate(dbLon, dbLat);
        ProjCoordinate after = new ProjCoordinate();

        return new BasicCoordinateTransform(grs80, wgs84).transform(before, after);
    }

    public static Point createPoint(String x, String y){
        ProjCoordinate castPoint = getCastPoint(x, y);
        return new GeometryFactory()
                .createPoint(new Coordinate(castPoint.x, castPoint.y));
    }
}
