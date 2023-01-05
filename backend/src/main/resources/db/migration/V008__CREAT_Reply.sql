create table if not exists reply
(
    article_id            bigint PRIMARY KEY AUTO_INCREMENT,
    user_id               bigint NOT NULL,
    article_id            bigint NOT NULL,
    sentece               varchar(255)  NOT NULL,
    created_at            datetime  NOT NULL,
    updated_at            datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE reply
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE article
    ADD FOREIGN KEY (article_id) REFERENCES article (article_id);