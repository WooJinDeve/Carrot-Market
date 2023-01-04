package com.carrot.application.region.repository;

import com.carrot.application.region.domain.Region;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

import static java.time.LocalDateTime.now;


@Repository
public class RegionJdbcRepository {
    private static final String TABLE = "region";

    private final JdbcTemplate jdbcTemplate;

    public RegionJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(List<Region> regions) {
        String sql = String.format(
                "INSERT INTO `%s` (region_code, region_name, location, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?)", TABLE);

        jdbcTemplate.batchUpdate(sql, regions, 500, (ps, argument) -> {
            ps.setString(1, argument.getRegionCode());
            ps.setString(2, argument.getName());
            ps.setObject(3, argument.getLocation());
            ps.setTimestamp(4, Timestamp.valueOf(now()));
            ps.setTimestamp(5, Timestamp.valueOf(now()));
        });
    }
}
