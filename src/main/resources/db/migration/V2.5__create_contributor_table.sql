CREATE TABLE IF NOT EXISTS contributors
(
    id SERIAL PRIMARY KEY,
    cid TEXT,
    org_name TEXT,
    total INTEGER,
    pacs INTEGER,
    indivs INTEGER
);