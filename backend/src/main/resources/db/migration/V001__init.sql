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
    created_at          datetime  NOT NULL,
    updated_at          datetime  NOT NULL
) engine = InnoDB;

create table if not exists region
(
    region_id          bigint PRIMARY KEY AUTO_INCREMENT,
    region_code        varchar(255) UNIQUE NOT NULL,
    region_name        varchar(50) NOT NULL,
    location           GEOMETRY NOT NULL,
    created_at         datetime  NOT NULL,
    updated_at         datetime NOT NULL
) engine = InnoDB;

create table if not exists user_region
(
    user_region_id      bigint PRIMARY KEY AUTO_INCREMENT,
    user_id             bigint NOT NULL,
    region_id           bigint NOT NULL,
    represent           bit Not Null,
    created_at          datetime  NOT NULL,
    updated_at          datetime  NOT NULL
) engine = InnoDB;

create table if not exists post
(
    post_id                bigint PRIMARY KEY AUTO_INCREMENT,
    user_id                bigint NOT NULL,
    region_id              bigint NOT NULL,
    title                  varchar(50) NOT NULL,
    content                LONGTEXT NOT NULL,
    price                  int NOT NULL,
    thumbnail              varchar(255) NOT NULL,
    category               varchar(255) NOT NULL,
    hits                   int NOT NULL,
    chat_num               int NOT NULL,
    article_num            int NOT NULL,
    deleted_at             datetime  null,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

create table if not exists post_image
(
    post_image_id          bigint PRIMARY KEY AUTO_INCREMENT,
    post_id                bigint NOT NULL,
    origin_name            varchar(255) NOT NULL,
    image_url              varchar(255) NOT NULL,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

create table if not exists post_like
(
    post_like_id           bigint PRIMARY KEY AUTO_INCREMENT,
    post_id                bigint NOT NULL,
    user_id                bigint NOT NULL,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

CREATE INDEX idx__name on region (region_name)

ALTER TABLE user_region
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE user_region
    ADD FOREIGN KEY (region_id) REFERENCES region (region_id) ON DELETE CASCADE;

ALTER TABLE post
    ADD FOREIGN KEY (region_id) REFERENCES region (region_id);

ALTER TABLE post
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE post_image
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE;

ALTER TABLE post_like
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE post_like
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE;