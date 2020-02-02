CREATE TABLE faculty
(
    id          UUID PRIMARY KEY NOT NULL,
    name        VARCHAR(50)      NOT NULL default '',
    description TEXT             NOT NULL DEFAULT ''
);

CREATE TABLE department
(
    id          UUID PRIMARY KEY             NOT NULL,
    faculty_id  UUID REFERENCES faculty (id) NOT NULL,
    name        VARCHAR(50)                  NOT NULL default '',
    description TEXT                         NOT NULL DEFAULT ''
);

CREATE TABLE training_direction
(
    id            UUID PRIMARY KEY                NOT NULL,
    department_id UUID REFERENCES department (id) NOT NULL,
    name          VARCHAR(50)                     NOT NULL default '',
    description   TEXT                            NOT NULL DEFAULT ''
);

CREATE TABLE std_group
(
    id                    UUID PRIMARY KEY                        NOT NULL,
    training_direction_id UUID REFERENCES training_direction (id) NOT NULL,
    name                  VARCHAR(50)                             NOT NULL default ''
);

CREATE TABLE users_groups
(
    user_id  UUID REFERENCES usr (id)       NOT NULL,
    group_id UUID REFERENCES std_group (id) NOT NULL,
    PRIMARY KEY (user_id, group_id)
);

CREATE TABLE teachers_training_directions
(
    teacher_id            UUID REFERENCES teacher (user_id)       NOT NULL,
    training_direction_id UUID REFERENCES training_direction (id) NOT NULL,
    PRIMARY KEY (teacher_id, training_direction_id)
)
