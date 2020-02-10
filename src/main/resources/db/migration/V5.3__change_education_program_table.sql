ALTER TABLE usr
    DROP COLUMN gender;

ALTER TABLE theme
    ADD COLUMN number SMALLINT NOT NULL DEFAULT 1;

ALTER TABLE training_direction
    ADD COLUMN code VARCHAR(10) NOT NULL default '';

ALTER TABLE education_program
    DROP CONSTRAINT education_program_direction_id_fkey;

ALTER TABLE education_program
    DROP COLUMN direction_id;

CREATE TABLE teachers_education_programs
(
    teacher_id           UUID REFERENCES teacher (user_id)      NOT NULL,
    education_program_id UUID REFERENCES education_program (id) NOT NULL,
    PRIMARY KEY (teacher_id, education_program_id)
);
