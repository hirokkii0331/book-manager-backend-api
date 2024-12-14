CREATE TYPE published_status AS ENUM ('UNPUBLISHED', 'PUBLISHED');

CREATE TABLE books (
    id bigint PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) CHECK (price >= 0),
    author VARCHAR(255) NOT NULL,
    status published_status NOT NULL
);
