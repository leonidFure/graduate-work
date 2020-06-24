ALTER TABLE courses_teachers
    ADD CONSTRAINT courses_teachers_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES usr (id);
ALTER TABLE teachers_education_programs
    ADD CONSTRAINT teachers_education_programs_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES usr (id);
ALTER TABLE teachers_education_programs
    ADD CONSTRAINT teachers_education_programs_education_program_id_fkey FOREIGN KEY (education_program_id) REFERENCES education_program (id);
ALTER TABLE teachers_faculties
    ADD CONSTRAINT teachers_faculties_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES usr (id);
