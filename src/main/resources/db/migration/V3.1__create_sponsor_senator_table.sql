CREATE TABLE IF NOT EXISTS sponsor_senators
(
    id         SERIAL PRIMARY KEY,
    sponsor_id INTEGER,
    senator_id INTEGER
)