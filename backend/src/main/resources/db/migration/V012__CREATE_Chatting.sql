create table if not exists chatting
(
    chat_id               bigint PRIMARY KEY AUTO_INCREMENT,
    chat_room_id          bigint NOT NULL,
    user_id               bigint NOT NULL,
    message               varchar(255)  NOT NULL,
    created_at            datetime  NOT NULL,
    updated_at            datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE chatting
    ADD FOREIGN KEY (chat_room_id) REFERENCES chat_room (chat_room_id) ON DELETE CASCADE;

ALTER TABLE chatting
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id);