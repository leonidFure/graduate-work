ALTER TABLE education_program
    drop column creation_date,
    add column creation_datetime timestamp not null default now();