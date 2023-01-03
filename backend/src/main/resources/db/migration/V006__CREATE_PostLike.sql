create table if not exists post_like
(
    post_like_id           bigint PRIMARY KEY AUTO_INCREMENT,
    post_id                bigint NOT NULL,
    user_id                bigint NOT NULL,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE post_like
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE post_like
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE;