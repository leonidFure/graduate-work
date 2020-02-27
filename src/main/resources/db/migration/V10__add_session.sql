CREATE TABLE session(
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID REFERENCES usr(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    token text NOT NULL,
    expiration_datetime TIMESTAMP NOT NULL
);