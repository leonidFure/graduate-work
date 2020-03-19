ALTER TABLE training_direction
    DROP CONSTRAINT training_direction_department_id_fkey;
ALTER TABLE training_direction
    DROP COLUMN department_id;
ALTER TABLE training_direction
    ADD COLUMN faculty_id UUID REFERENCES faculty (id) ON DELETE CASCADE ON UPDATE CASCADE;

DROP TABLE department