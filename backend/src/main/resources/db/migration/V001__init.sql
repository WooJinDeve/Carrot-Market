create table if not exists users
(
    user_id            bigint PRIMARY KEY AUTO_INCREMENT,
    email              varchar(255) UNIQUE NOT NULL,
    manner_temperature double       NOT NULL,
    nickname           varchar(15)  NOT NULL,
    profile_url        varchar(255) NOT NULL,
    provider           varchar(255) null,
    provider_id        varchar(255) null,
    role               varchar(255) NOT NULL,
    deleted_at         datetime  null,
    certificated_at    datetime  null,
    created_at          datetime  NOT NULL,
    updated_at          datetime  NOT NULL
) engine = InnoDB;

create table if not exists region
(
    region_id          bigint PRIMARY KEY AUTO_INCREMENT,
    region_code        varchar(255) UNIQUE NOT NULL,
    region_name        varchar(50) NOT NULL,
    location           point NOT NULL,
    created_at          datetime  NOT NULL,
    updated_at          datetime NOT NULL
) engine = InnoDB;

create table if not exists user_region
(
    user_region_id     bigint PRIMARY KEY AUTO_INCREMENT,
    user_id            bigint NOT NULL,
    region_id          bigint NOT NULL,
    created_at          datetime  NOT NULL,
    updated_at          datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE user_region
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE user_region
    ADD FOREIGN KEY (region_id) REFERENCES region (region_id) ON DELETE CASCADE;
