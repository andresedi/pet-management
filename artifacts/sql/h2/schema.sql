CREATE TABLE pet (
    pet_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(100) NOT NULL,
    age INT,
    owner_name VARCHAR(100)
);
CREATE INDEX ON pet (pet_id);