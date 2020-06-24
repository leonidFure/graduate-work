CREATE TABLE file
(
    id                    UUID PRIMARY KEY NOT NULL,
    name                  VARCHAR          NOT NULL,
    extension             VARCHAR(10)      NOT NULL,
    type                  VARCHAR(20)      NOT NULL,
    uploading_date_time   TIMESTAMP        NOT NULL,
    last_update_date_time TIMESTAMP
);

ALTER TABLE usr
    ADD COLUMN image_id UUID REFERENCES file (id);

ALTER TABLE usr
    DROP COLUMN IF EXISTS photo_exists ;

ALTER TABLE subject
    ADD COLUMN image_id UUID REFERENCES file (id);

ALTER TABLE course
    ADD COLUMN image_id UUID REFERENCES file (id);

CREATE TABLE lessons_files
(
    lesson_id UUID REFERENCES lesson (id) NOT NULL,
    file_id   UUID REFERENCES file (id)   NOT NULL,
    PRIMARY KEY (lesson_id, file_id)
);

