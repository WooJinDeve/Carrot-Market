INSERT INTO region(region_code, region_name, location, created_at, updated_at)
VALUES ("10000", "서울", ST_GeomFromText('POINT(1 1)') , now(), now());

