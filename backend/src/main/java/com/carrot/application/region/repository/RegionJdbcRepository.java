package com.carrot.application.region.repository;

import com.carrot.application.region.domain.Region;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionJdbcRepository {

    private static final String TABLE = "region";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RegionJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void bulkInsert(List<Region> regions) {
        String sql = String.format(
                "INSERT INTO `%s` (region_code, region_name, location, created_at, updated_at) " +
                "VALUES (:regionCode, :name, ST_GeomFromText(':location'), :createdAt, :updatedAt)", TABLE);

        SqlParameterSource[] params = regions.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
