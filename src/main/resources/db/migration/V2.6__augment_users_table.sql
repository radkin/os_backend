ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS password VARCHAR(255);
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS state VARCHAR(2);
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS gender VARCHAR(5);
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS party VARCHAR(50);
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS is_enabled BOOLEAN;