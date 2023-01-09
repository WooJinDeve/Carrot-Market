create table if not exists region
(
    region_id          bigint PRIMARY KEY AUTO_INCREMENT,
    region_code        varchar(255) UNIQUE NOT NULL,
    region_name        varchar(50) NOT NULL,
    location           GEOMETRY NOT NULL,
    created_at         datetime  NOT NULL,
    updated_at         datetime NOT NULL
) engine = InnoDB;

CREATE INDEX idx__name on region (region_name);
CREATE INDEX idx__location on region (location);