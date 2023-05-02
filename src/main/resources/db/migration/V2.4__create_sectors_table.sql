CREATE TABLE IF NOT EXISTS sectors
(
    id SERIAL PRIMARY KEY,
    cid TEXT,
    sector_name TEXT,
    sectorid TEXT,
    indivs INTEGER,
    pacs INTEGER,
    total INTEGER
);