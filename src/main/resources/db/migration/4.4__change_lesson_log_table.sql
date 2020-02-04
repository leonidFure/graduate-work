DROP TABLE lesson_log;

CREATE TABLE lesson_log
(
    id         UUID PRIMARY KEY            NOT NULL,
    lesson_id  UUID REFERENCES lesson (id) NOT NULL,
    datetime   TIMESTAMP                   NOT NULL,
    old_status VARCHAR(50)                 NOT NULL,
    new_status VARCHAR(50)                 NOT NULL
);