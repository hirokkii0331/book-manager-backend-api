CREATE TABLE authors (
    id bigint PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL CHECK (birth_date < CURRENT_DATE)
);
