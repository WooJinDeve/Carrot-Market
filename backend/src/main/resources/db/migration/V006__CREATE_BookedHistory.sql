create table if not exists booked_history
(
    bh_id                  bigint PRIMARY KEY AUTO_INCREMENT,
    seller_id               bigint NOT NULL,
    booker_id              bigint NOT NULL,
    post_id                bigint NOT NULL,
    thumbnail              varchar(255) NOT NULL,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE booked_history
    ADD FOREIGN KEY (seller_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE booked_history
    ADD FOREIGN KEY (booker_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE booked_history
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE;