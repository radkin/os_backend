CREATE TABLE IF NOT EXISTS committees
(
    id                          SERIAL PRIMARY KEY,
    fec_committee_id            TEXT,
    two_year_transaction_period INTEGER,
    pp_id                       TEXT
)