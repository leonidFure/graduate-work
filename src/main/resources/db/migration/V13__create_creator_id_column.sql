ALTER TABLE course
    ADD COLUMN creator_id UUID REFERENCES usr (id) ON DELETE SET NULL ON UPDATE CASCADE;