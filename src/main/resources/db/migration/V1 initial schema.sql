-- V1__initial_schema.sql

CREATE TABLE users (
                       id         BIGSERIAL       PRIMARY KEY,
                       name       VARCHAR(100)    NOT NULL,
                       email      VARCHAR(150)    NOT NULL UNIQUE,
                       created_at TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE TABLE tasks (
                       id          BIGSERIAL       PRIMARY KEY,
                       title       VARCHAR(200)    NOT NULL,
                       description TEXT,
                       status      VARCHAR(20)     NOT NULL DEFAULT 'TODO',
                       due_date    DATE,
                       user_id     BIGINT          NOT NULL REFERENCES users(id),
                       created_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);