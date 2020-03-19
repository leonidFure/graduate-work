ALTER TABLE lesson
    DROP CONSTRAINT lesson_course_id_fkey,
    DROP CONSTRAINT lesson_timetable_id_fkey,
    ADD CONSTRAINT lesson_course_id_fkey FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT lesson_timetable_id_fkey FOREIGN KEY (timetable_id) REFERENCES timetable (id) ON DELETE CASCADE ON UPDATE CASCADE;
