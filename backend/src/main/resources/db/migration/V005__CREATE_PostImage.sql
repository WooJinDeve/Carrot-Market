create table if not exists post_image
(
    post_image_id          bigint PRIMARY KEY AUTO_INCREMENT,
    post_id                bigint NOT NULL,
    origin_name            varchar(255) NOT NULL,
    image_url              varchar(255) NOT NULL,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE post_image
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE;