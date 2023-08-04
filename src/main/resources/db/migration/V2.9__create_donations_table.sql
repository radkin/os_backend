CREATE TABLE IF NOT EXISTS donations
(
    id                SERIAL PRIMARY KEY,
    amount            INTEGER,
    date_of_donation  DATE,
    sponsor_id        INTEGER,
    chamber_member_id INTEGER
)