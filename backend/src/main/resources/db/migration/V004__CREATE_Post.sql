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
    statue                 varchar(255) NOT NULL,
    hits                   int NOT NULL,
    chat_num               int NOT NULL,
    article_num            int NOT NULL,
    deleted_at             datetime  null,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

CREATE INDEX idx__title on post (title);
CREATE INDEX idx__category on post (category);

ALTER TABLE post
    ADD FOREIGN KEY (region_id) REFERENCES region (region_id);

ALTER TABLE post
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;