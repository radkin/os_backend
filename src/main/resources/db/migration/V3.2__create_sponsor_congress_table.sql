CREATE TABLE IF NOT EXISTS sponsor_congress
(
    id          SERIAL PRIMARY KEY,
    sponsor_id  INTEGER,
    congress_id INTEGER
)