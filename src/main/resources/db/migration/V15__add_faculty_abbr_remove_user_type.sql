ALTER TABLE usr DROP COLUMN user_type;
ALTER TABLE faculty ADD COLUMN abbr VARCHAR(6) NOT NULL DEFAULT '';