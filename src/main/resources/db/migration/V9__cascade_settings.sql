ALTER TABLE course
    DROP CONSTRAINT course_education_program_id_fkey,
    ADD CONSTRAINT course_education_program_id_fkey FOREIGN KEY (education_program_id) REFERENCES education_program (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE courses_teachers
    DROP CONSTRAINT courses_teachers_course_id_fkey,
    DROP CONSTRAINT courses_teachers_teacher_id_fkey,
    ADD CONSTRAINT courses_teachers_course_id_fkey FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT courses_teachers_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES teacher (user_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE education_program
    DROP CONSTRAINT education_program_subject_id_fkey,
    ADD CONSTRAINT education_program_subject_id_fkey FOREIGN KEY (subject_id) REFERENCES subject (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE faculty
    DROP CONSTRAINT faculty_manager_id_fkey,
    ADD CONSTRAINT faculty_manager_id_fkey FOREIGN KEY (manager_id) REFERENCES usr (id) ON DELETE SET DEFAULT ON UPDATE CASCADE;

ALTER TABLE lesson
    DROP CONSTRAINT lesson_course_id_fkey,
    DROP CONSTRAINT lesson_timetable_id_fkey,
    ADD CONSTRAINT lesson_course_id_fkey FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT lesson_timetable_id_fkey FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lesson_log
    DROP CONSTRAINT lesson_log_lesson_id_fkey,
    ADD CONSTRAINT lesson_log_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES lesson (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lessons_themes
    DROP CONSTRAINT lessons_themes_lesson_id_fkey,
    DROP CONSTRAINT lessons_themes_theme_id_fkey,
    ADD CONSTRAINT lessons_themes_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES lesson (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT lessons_themes_theme_id_fkey FOREIGN KEY (theme_id) REFERENCES theme (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE subjects_for_entrance
    DROP CONSTRAINT subjects_for_entrance_direction_id_fkey,
    DROP CONSTRAINT subjects_for_entrance_subject_id_fkey,
    ADD CONSTRAINT subjects_for_entrance_direction_id_fkey FOREIGN KEY (direction_id) REFERENCES training_direction (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT subjects_for_entrance_subject_id_fkey FOREIGN KEY (subject_id) REFERENCES subject (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE teacher
    DROP CONSTRAINT teacher_user_id_fkey,
    ADD CONSTRAINT teacher_user_id_fkey FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE teachers_education_programs
    DROP CONSTRAINT teachers_education_programs_education_program_id_fkey,
    DROP CONSTRAINT teachers_education_programs_teacher_id_fkey,
    ADD CONSTRAINT teachers_education_programs_education_program_id_fkey FOREIGN KEY (teacher_id) REFERENCES teacher (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT teachers_education_programs_teacher_id_fkey FOREIGN KEY (education_program_id) REFERENCES education_program (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE teachers_faculties
    DROP CONSTRAINT teachers_faculties_faculty_id_fkey,
    DROP CONSTRAINT teachers_faculties_teacher_id_fkey,
    ADD CONSTRAINT teachers_faculties_faculty_id_fkey FOREIGN KEY (faculty_id) REFERENCES faculty (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT teachers_faculties_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES teacher (user_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE theme
    DROP CONSTRAINT theme_parent_theme_fkey,
    DROP CONSTRAINT theme_education_program_id_fkey,
    ADD CONSTRAINT theme_parent_theme_fkey FOREIGN KEY (parent_theme_id) REFERENCES theme (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT theme_education_program_id_fkey FOREIGN KEY (education_program_id) REFERENCES education_program (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE timetable
    DROP CONSTRAINT timetable_course_id_fkey,
    ADD CONSTRAINT timetable_course_id_fkey FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE training_direction
    DROP CONSTRAINT training_direction_faculty_id_fkey,
    ADD CONSTRAINT training_direction_faculty_id_fkey FOREIGN KEY (faculty_id) REFERENCES faculty (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE user_roles
    DROP CONSTRAINT usr_user_roles,
    ADD CONSTRAINT usr_user_roles FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE ON UPDATE CASCADE;

