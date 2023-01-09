create table if not exists transaction_history
(
    th_id                  bigint PRIMARY KEY AUTO_INCREMENT,
    seller_id              bigint NOT NULL,
    buyer_id               bigint NOT NULL,
    post_id                bigint NOT NULL,
    thumbnail              varchar(255) NOT NULL,
    created_at             datetime  NOT NULL,
    updated_at             datetime  NOT NULL
) engine = InnoDB;

ALTER TABLE transaction_history
    ADD FOREIGN KEY (seller_id) REFERENCES users (user_id);

ALTER TABLE transaction_history
    ADD FOREIGN KEY (buyer_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE transaction_history
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id);