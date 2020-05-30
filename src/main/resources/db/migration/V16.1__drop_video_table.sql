DROP table video;
ALTER TABLE lesson
    ADD COLUMN video_uri VARCHAR UNIQUE;