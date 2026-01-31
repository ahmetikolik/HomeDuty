-- 1) Şifre hashlemek için
CREATE EXTENSION IF NOT EXISTS pgcrypto;


ALTER TABLE homeduty.person
ADD COLUMN IF NOT EXISTS password_hash text;


UPDATE homeduty.person
SET password_hash = crypt('12345', gen_salt('bf'))
WHERE password_hash IS NULL;

-- 2) Login fonksiyonu: email + sifre -> kullanıcı döndürür
DROP FUNCTION IF EXISTS homeduty.fn_login(text, text);

CREATE FUNCTION homeduty.fn_login(p_email text, p_password text)
RETURNS TABLE(
  person_id int,
  family_id int,
  full_name varchar,
  email varchar,
  role_type varchar,
  total_points int
)
LANGUAGE sql
AS $$
  SELECT person_id, family_id, full_name, email, role_type, total_points
  FROM homeduty.person
  WHERE email = p_email
    AND password_hash = crypt(p_password, password_hash);
$$;

GRANT EXECUTE ON FUNCTION homeduty.fn_login(text, text) TO PUBLIC;
GRANT EXECUTE ON FUNCTION homeduty.fn_assign_duty_by_name(int, text, date) TO PUBLIC;
GRANT EXECUTE ON FUNCTION homeduty.fn_search_person(text) TO PUBLIC;


GRANT SELECT ON ALL TABLES IN SCHEMA homeduty TO PUBLIC;


GRANT UPDATE ON TABLE homeduty.duty TO PUBLIC;


GRANT INSERT, SELECT ON TABLE homeduty.duty_log TO PUBLIC;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE homeduty.duty_log_log_id_seq TO PUBLIC;


GRANT INSERT ON TABLE homeduty.duty TO PUBLIC;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE homeduty.seq_duty_no TO PUBLIC;


