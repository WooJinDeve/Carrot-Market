package com.carrot.application.user.domain;

import com.carrot.infrastructure.util.ClassUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class MannerTemperature {

    private static final double MAX_MANNER_TEMPERATURE = 99;
    private static final double MIN_MANNER_TEMPERATURE = 0;
    private static final double DEFAULT_MANNER_TEMPERATURE = 36.5;

    @Column(name = "manner_temperature", nullable = false)
    private Double mannerTemperature;

    public MannerTemperature(Double mannerTemperature) {
        ClassUtils.checkNotNullParameter(mannerTemperature, Double.class);
        this.mannerTemperature = mannerTemperature;
    }

    public static MannerTemperature create(){
        return new MannerTemperature(DEFAULT_MANNER_TEMPERATURE);
    }

}
