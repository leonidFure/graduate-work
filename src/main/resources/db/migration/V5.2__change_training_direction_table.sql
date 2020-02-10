DROP TABLE teachers_training_directions;

CREATE TABLE teachers_faculties
(
    faculty_id  UUID REFERENCES faculty (id)       NOT NULL,
    teacher_id UUID REFERENCES teacher (user_id) NOT NULL,
    PRIMARY KEY (faculty_id, teacher_id)
);

CREATE TABLE subjects_for_entrance
(
    subject_id  UUID REFERENCES subject (id)       NOT NULL,
    direction_id UUID REFERENCES training_direction (id) NOT NULL,
    PRIMARY KEY (subject_id, direction_id)
);