CREATE OR REPLACE FUNCTION fn_search_person(p_name TEXT)
RETURNS TABLE(person_id INT, full_name VARCHAR, email VARCHAR, total_points INT)
LANGUAGE sql
AS $$
  SELECT person_id, full_name, email, total_points
  FROM homeduty.person
  WHERE full_name ILIKE '%' || p_name || '%'
  ORDER BY full_name;
$$;
