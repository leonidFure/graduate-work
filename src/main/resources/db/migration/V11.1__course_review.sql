CREATE TABLE course_review
(
    course_id UUID REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    user_id   UUID REFERENCES usr (id) ON DELETE CASCADE ON UPDATE CASCADE    NOT NULL,
    rating SMALLINT NOT NULL,
    comment_head VARCHAR(255) NOT NULL,
    comment_body TEXT NOT NULL,
    PRIMARY KEY (course_id, user_id)
);