create table if not exists notification
(
    notification_id       bigint PRIMARY KEY AUTO_INCREMENT,
    receiver_id           bigint NOT NULL,
    type                  varchar(255) NOT NULL,
    args                  json  NOT NULL,
    deleted_at            datetime  NULL,
    created_at            datetime  NOT NULL,
    updated_at            datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE notification
    ADD FOREIGN KEY (receiver_id) REFERENCES users (user_id);