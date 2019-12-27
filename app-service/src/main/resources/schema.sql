CREATE TABLE IF NOT EXISTS users (
    id UUID NOT NULL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    enabled BOOLEAN,
    created_timestamp TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id UUID NOT NULL PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_role (
    user_id UUID,
    role_id UUID,
    PRIMARY KEY (user_id, role_id)
)