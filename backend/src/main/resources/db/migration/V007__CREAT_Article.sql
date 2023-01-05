create table if not exists article
(
    article_id            bigint PRIMARY KEY AUTO_INCREMENT,
    user_id               bigint NOT NULL,
    post_id               bigint NOT NULL,
    sentece               varchar(255)  NOT NULL,
    deleted_at            datetime  null,
    created_at            datetime  NOT NULL,
    updated_at            datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE article
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE article
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE;