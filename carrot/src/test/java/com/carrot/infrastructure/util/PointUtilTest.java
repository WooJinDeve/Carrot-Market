package com.carrot.infrastructure.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class PointUtilTest {

    @DisplayName("EPSG:5179 좌표 변환")
    @Test
    void 좌표변환() {
        //given
        String x = "954919";
        String y = "1950195";

        //when & then
        assertThatCode(() -> PointUtil.createPoint(x, y));
    }
}