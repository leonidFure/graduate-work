CREATE TABLE video
(
    user_id   UUID REFERENCES usr (id) ON DELETE CASCADE ON UPDATE CASCADE    NOT NULL,
    lesson_id UUID REFERENCES lesson (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    course_id UUID REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    video_uri VARCHAR                                                         NOT NULL,
    PRIMARY KEY (lesson_id, video_uri)
);
