ALTER TABLE courses_teachers DROP CONSTRAINT courses_teachers_teacher_id_fkey;
ALTER TABLE teachers_education_programs DROP CONSTRAINT teachers_education_programs_teacher_id_fkey;
ALTER TABLE teachers_education_programs DROP CONSTRAINT teachers_education_programs_education_program_id_fkey;
ALTER TABLE teachers_faculties DROP CONSTRAINT teachers_faculties_teacher_id_fkey;
DROP TABLE teacher;
DROP TABLE user_roles;

ALTER TABLE usr ADD COLUMN role VARCHAR(50);
ALTER TABLE usr ADD COLUMN user_type VARCHAR(20);
ALTER TABLE usr ADD COLUMN start_work_date DATE;
ALTER TABLE usr ADD COLUMN info TEXT;