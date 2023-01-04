package com.carrot.application.region.repository;

import com.carrot.application.region.domain.Region;
import com.carrot.infrastructure.util.CsvUtil;
import com.carrot.infrastructure.util.PointUtil;
import com.carrot.testutil.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;


class RegionJdbcRepositoryTest extends RepositoryTest {
    private static final String CSV_LOCATION = "/csv/location.csv";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RegionJdbcRepository regionJdbcRepository;

    @BeforeEach
    public void init(){
        regionJdbcRepository = new RegionJdbcRepository(jdbcTemplate);
    }


    @DisplayName("[Success] 지역 데이터 벌크 Insert")
    @Test
    void 지역데이터_벌크_Insert() {
        //given
        List<Region> regions = convertToRegions();

        //when & then
        assertThatCode(() -> regionJdbcRepository.batchInsert(regions))
                .doesNotThrowAnyException();
    }

    private List<Region> convertToRegions(){
        return Objects.requireNonNull(CsvUtil.readCsvLines(CSV_LOCATION))
                .stream()
                .map(this::convertToRegion)
                .collect(Collectors.toList());
    }

    private Region convertToRegion(String[] regions){
        return Region.of(regions[0], regions[1], PointUtil.createPoint(regions[2], regions[3]));
    }
}