-- Rolleri oluştur
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname='homeduty_admin') THEN
    CREATE ROLE homeduty_admin LOGIN PASSWORD 'admin123';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname='homeduty_user') THEN
    CREATE ROLE homeduty_user LOGIN PASSWORD 'user123';
  END IF;
END $$;

-- Yetkiler
GRANT USAGE ON SCHEMA homeduty TO homeduty_admin, homeduty_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA homeduty TO homeduty_admin;
GRANT SELECT ON ALL TABLES IN SCHEMA homeduty TO homeduty_user;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA homeduty TO homeduty_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA homeduty TO homeduty_admin, homeduty_user;

-- User role insert/update/delete yapamasın (sadece listeleme/arama)
REVOKE INSERT, UPDATE, DELETE ON homeduty.duty FROM homeduty_user;
REVOKE INSERT, UPDATE, DELETE ON homeduty.person FROM homeduty_user;
