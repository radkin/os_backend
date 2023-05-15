CREATE TABLE IF NOT EXISTS contributors
(
    id SERIAL PRIMARY KEY,
    cid TEXT,
    cycle INTEGER,
    org_name TEXT,
    contributorid TEXT,
    total INTEGER,
    pacs INTEGER,
    indivs INTEGER
);