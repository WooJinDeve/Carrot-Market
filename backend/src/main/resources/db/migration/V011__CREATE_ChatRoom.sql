create table if not exists chat_room
(
    chat_room_id            bigint PRIMARY KEY AUTO_INCREMENT,
    seller_id               bigint NOT NULL,
    buyer_id                bigint NOT NULL,
    post_id                 bigint NOT NULL,
    deleted_at              datetime  null,
    created_at              datetime  NOT NULL,
    updated_at              datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE chat_room
    ADD FOREIGN KEY (seller_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE chat_room
    ADD FOREIGN KEY (buyer_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE chat_room
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE;;