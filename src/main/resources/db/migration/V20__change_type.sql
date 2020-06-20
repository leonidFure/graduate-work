ALTER TABLE course
    ADD COLUMN creation_datetime timestamp Not Null default now();
alter table course drop column creation_date;