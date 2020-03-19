CREATE TABLE subject
(
    id          UUID PRIMARY KEY              NOT NULL,
    name        VARCHAR(50) UNIQUE DEFAULT '' NOT NULL,
    description TEXT               DEFAULT '' NOT NULL,
    type        VARCHAR(10)                   NOT NULL
);

CREATE TABLE education_program
(
    id            UUID PRIMARY KEY                        NOT NULL,
    direction_id  UUID REFERENCES training_direction (id) NOT NULL,
    subject_id    UUID REFERENCES subject (id)            NOT NULL,
    name          VARCHAR(50) UNIQUE DEFAULT ''           NOT NULL,
    description   TEXT               DEFAULT ''           NOT NULL,
    creation_date DATE               DEFAULT NOW()        NOT NULL,
    status        VARCHAR(50)                             NOT NULL,
    is_actual     BOOL                                    NOT NULL
);

CREATE TABLE course
(
    id                   UUID PRIMARY KEY                       NOT NULL,
    education_program_id UUID REFERENCES education_program (id) NOT NULL,
    status               VARCHAR(50)                            NOT NULL,
    start_date           DATE                                   NOT NULL,
    end_date             DATE                                   NOT NULL,
    creation_date        DATE DEFAULT NOW()                     NOT NULL,
    is_actual            BOOL                                   NOT NULL
);

CREATE TABLE courses_teachers
(
    course_id  UUID REFERENCES course (id)       NOT NULL,
    teacher_id UUID REFERENCES teacher (user_id) NOT NULL,
    PRIMARY KEY (course_id, teacher_id)
);

CREATE TABLE timetable
(
    id          UUID PRIMARY KEY            NOT NULL,
    course_id   UUID REFERENCES course (id) NOT NULL,
    day_of_week VARCHAR(3)                  NOT NULL,
    start_time  TIME                        NOT NULL,
    end_time    TIME                        NOT NULL,
    type        VARCHAR(10)                 NOT NULL,
    is_actual   BOOL                        NOT NULL
);

CREATE TABLE lesson
(
    id        UUID PRIMARY KEY            NOT NULL,
    course_id UUID REFERENCES course (id) NOT NULL,
    date      DATE                        NOT NULL,
    status    VARCHAR(50)                 NOT NULL
);

CREATE TABLE theme
(
    id                   UUID PRIMARY KEY                       NOT NULL,
    parent_theme         UUID REFERENCES theme (id)             NOT NULL,
    education_program_id UUID REFERENCES education_program (id) NOT NULL,
    name                 VARCHAR(50) UNIQUE DEFAULT ''          NOT NULL,
    description          TEXT               DEFAULT ''          NOT NULL
);

CREATE TABLE lessons_themes
(
    lesson_id UUID REFERENCES lesson (id) NOT NULL,
    theme_id  UUID REFERENCES theme (id)  NOT NULL,
    PRIMARY KEY (lesson_id, theme_id)
);

CREATE TABLE lesson_log
(
    lesson_id  UUID REFERENCES lesson (id) NOT NULL,
    datetime   TIMESTAMP                   NOT NULL,
    old_status VARCHAR(50)                 NOT NULL,
    new_status VARCHAR(50)                 NOT NULL
);