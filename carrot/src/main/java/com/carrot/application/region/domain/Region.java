package com.carrot.application.region.domain;

import com.carrot.application.common.BaseEntity;
import com.carrot.global.error.CarrotRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

import static com.carrot.global.error.ErrorCode.REGION_LENGTH_VALIDATION_ERROR;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "region", indexes = @Index(name = "idx__name", columnList = "region_name"))
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Region extends BaseEntity {
    private static final int MAX_REGION_NAME_LENGTH = 50;

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "region_id", nullable = false)
    private Long id;

    @Column(name = "region_code", unique = true, nullable = false)
    private String regionCode;

    @Column(name = "region_name", nullable = false, length = MAX_REGION_NAME_LENGTH)
    private String name;

    @Column(name = "location", columnDefinition = "POINT")
    private Point location;

    @Builder
    public Region(Long id, String regionCode, String name, Point location) {
        validateName(name);
        this.id = id;
        this.regionCode = regionCode;
        this.name = name;
        this.location = location;
    }

    public static Region of(String regionCode, String name, Point location){
        return Region.builder()
                .regionCode(regionCode)
                .name(name)
                .location(location)
                .build();
    }

    private void validateName(String name){
        if (name.length() > MAX_REGION_NAME_LENGTH)
            throw new CarrotRuntimeException(REGION_LENGTH_VALIDATION_ERROR);
    }
}
