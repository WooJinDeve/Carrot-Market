package com.carrot.infrastructure.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class CsvUtilTest {

    @DisplayName("지역 정보 csv 데이터 읽기")
    @Test
    void 지역정보_csv_파일변환() throws Exception {
        //given
        final String CSV_LOCATION = "/csv/location.csv";

        //when & then
        assertThatCode(() -> CsvUtil.readCsvLines(CSV_LOCATION));
    }
}