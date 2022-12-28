package com.carrot.application.region.repository;

import com.carrot.application.region.domain.Region;
import com.carrot.infrastructure.util.CsvUtil;
import com.carrot.infrastructure.util.PointUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
class RegionBulkInsertRepositoryTest{

    @Autowired
    private RegionRepository regionRepository;

    @DisplayName("지역정보 벌크 Insert")
    @Test
    void 벌크_Insert() throws Exception {
        final String CSV_LOCATION = "/csv/location.csv";

        List<Region> regions = CsvUtil.readCsvLines(CSV_LOCATION)
                .stream().map(read -> Region.of(read[0], read[1], PointUtil.createPoint(read[2], read[3])))
                .collect(Collectors.toList());

        regionRepository.saveAll(regions);
//        assertThatCode(() -> regionJdbcRepository.bulkInsert(regions));
    }
}