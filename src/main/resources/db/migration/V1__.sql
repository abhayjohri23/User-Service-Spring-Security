CREATE TABLE sessions
(
    entity_id        BIGINT NOT NULL,
    user_id          BIGINT NULL,
    date_of_issuance date NULL,
    time_of_issuance time NULL,
    token            VARCHAR(255) NULL,
    session_status   INT    NOT NULL,
    CONSTRAINT pk_sessions PRIMARY KEY (entity_id)
);

CREATE TABLE users
(
    entity_id       BIGINT NOT NULL,
    username        VARCHAR(255) NULL,
    bcrypt_password VARCHAR(255) NULL,
    CONSTRAINT pk_users PRIMARY KEY (entity_id)
);

ALTER TABLE sessions
    ADD CONSTRAINT FK_SESSIONS_ON_USERID FOREIGN KEY (user_id) REFERENCES users (entity_id);