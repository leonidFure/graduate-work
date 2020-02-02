CREATE TABLE usr (
    id UUID NOT NULL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    birth_date DATE NOT NULL,
    gender VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active BOOL NOT NULL,
    registration_date DATE NOT NULL DEFAULT NOW()
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES usr (id),
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);

CREATE TABLE teacher (
    user_id UUID NOT NULL REFERENCES usr (id) PRIMARY KEY,
    start_work_date DATE NOT NULL,
    info TEXT NOT NULL DEFAULT ''
);

CREATE TABLE education (
    id UUID NOT NULL,
    user_id UUID NOT NULL REFERENCES usr (id),
    education_place VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    speciality VARCHAR (255) NOT NULL DEFAULT '',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    PRIMARY KEY (id, user_id)
);