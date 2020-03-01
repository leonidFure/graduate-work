CREATE TABLE course_subscription
(
    course_id UUID REFERENCES course (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    user_id   UUID REFERENCES usr (id) ON DELETE CASCADE ON UPDATE CASCADE    NOT NULL,
    PRIMARY KEY (course_id, user_id)
);