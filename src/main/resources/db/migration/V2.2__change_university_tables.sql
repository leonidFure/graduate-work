ALTER TABLE faculty ADD CONSTRAINT unique_faculty_name UNIQUE (name);
ALTER TABLE department ADD CONSTRAINT unique_department_name UNIQUE (name);
ALTER TABLE training_direction ADD CONSTRAINT unique_training_dir_name UNIQUE (name);
ALTER TABLE std_group ADD COLUMN creation_year SMALLINT NOT NULL DEFAULT 0;