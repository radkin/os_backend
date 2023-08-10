CREATE TABLE IF NOT EXISTS donations
(
    id               SERIAL PRIMARY KEY,
    amount           NUMERIC(18, 2),
    date_of_donation DATE,
    sponsor_id       INTEGER,
    pp_id            TEXT
)