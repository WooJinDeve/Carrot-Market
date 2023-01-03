create table if not exists user_region
(
    user_region_id      bigint PRIMARY KEY AUTO_INCREMENT,
    user_id             bigint NOT NULL,
    region_id           bigint NOT NULL,
    represent           bit Not Null,
    created_at          datetime  NOT NULL,
    updated_at          datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE user_region
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE user_region
    ADD FOREIGN KEY (region_id) REFERENCES region (region_id) ON DELETE CASCADE;