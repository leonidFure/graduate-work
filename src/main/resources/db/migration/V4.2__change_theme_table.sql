ALTER TABLE theme
ALTER COLUMN parent_theme DROP NOT NULL;
ALTER TABLE theme
RENAME COLUMN parent_theme TO parent_theme_id;
