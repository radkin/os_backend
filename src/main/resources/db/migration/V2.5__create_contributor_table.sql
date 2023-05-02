CREATE TABLE IF NOT EXISTS contributors
(
    id SERIAL PRIMARY KEY,
    org_name TEXT,
    total INTEGER,
    pacs INTEGER,
    indivs INTEGER
);