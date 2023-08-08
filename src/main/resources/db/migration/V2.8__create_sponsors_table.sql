CREATE TABLE IF NOT EXISTS sponsors
(
    id                          SERIAL PRIMARY KEY,
    contribution_receipt_amount TEXT,
    contribution_receipt_date   TEXT,
    contributor_aggregate_ytd   TEXT,
    contributor_city            TEXT,
    contributor_employer        TEXT,
    contributor_first_name      TEXT,
    contributor_last_name       TEXT,
    contributor_middle_name     TEXT,
    contributor_name            TEXT,
    contributor_occupation      TEXT,
    contributor_state           TEXT,
    contributor_street_1        TEXT,
    contributor_street_2        TEXT,
    contributor_zip             TEXT
)