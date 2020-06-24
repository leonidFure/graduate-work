ALTER TABLE lessons_files
    DROP CONSTRAINT lessons_files_lesson_id_fkey,
    DROP CONSTRAINT lessons_files_file_id_fkey,
    ADD CONSTRAINT lessons_files_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES lesson (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT lessons_files_file_id_fkey FOREIGN KEY (file_id) REFERENCES file (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE subject
    DROP CONSTRAINT subject_image_id_fkey,
    ADD CONSTRAINT subject_image_id_fkey FOREIGN KEY (image_id) REFERENCES file (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE teachers_education_programs
    DROP CONSTRAINT teachers_education_programs_teacher_id_fkey,
    DROP CONSTRAINT teachers_education_programs_education_program_id_fkey,
    ADD CONSTRAINT teachers_education_programs_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES usr (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT teachers_education_programs_education_program_id_fkey FOREIGN KEY (education_program_id) REFERENCES education_program (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE usr
    DROP CONSTRAINT usr_image_id_fkey,
    ADD CONSTRAINT usr_image_id_fkey FOREIGN KEY (image_id) REFERENCES file (id) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE teachers_faculties
    DROP CONSTRAINT teachers_faculties_teacher_id_fkey,
    DROP CONSTRAINT teachers_faculties_faculty_id_fkey,
    ADD CONSTRAINT teachers_faculties_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES usr (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT teachers_faculties_faculty_id_fkey FOREIGN KEY (faculty_id) REFERENCES faculty (id) ON DELETE CASCADE ON UPDATE CASCADE;


