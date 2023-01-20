CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY,
    inajar_api_key TEXT,
    email TEXT,
    first_name TEXT,
    last_name TEXT,
    profile_img TEXT
);