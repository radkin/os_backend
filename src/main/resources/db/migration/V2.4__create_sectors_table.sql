CREATE TABLE IF NOT EXISTS sectors
(
    id SERIAL PRIMARY KEY,
    sector_name TEXT,
    sectorid TEXT,
    indivs INTEGER,
    pacs INTEGER,
    total INTEGER
);