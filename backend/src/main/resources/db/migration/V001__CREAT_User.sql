create table if not exists users
(
    user_id            bigint PRIMARY KEY AUTO_INCREMENT,
    email              varchar(255) UNIQUE NOT NULL,
    manner_temperature double       NOT NULL,
    nickname           varchar(15)  NOT NULL,
    profile_url        varchar(255) NOT NULL,
    provider           varchar(255) null,
    provider_id        varchar(255) null,
    role               varchar(255) NOT NULL,
    deleted_at         datetime  null,
    created_at          datetime  NOT NULL,
    updated_at          datetime  NOT NULL
) engine = InnoDB;