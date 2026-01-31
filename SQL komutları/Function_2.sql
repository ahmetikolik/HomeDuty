CREATE OR REPLACE FUNCTION fn_assign_duty(p_family_id INT, p_task_id INT, p_due DATE)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
  v_person_id INT;
  v_duty_id INT;
BEGIN
  -- En düşük puanlı kişiye ata 
  SELECT person_id INTO v_person_id
  FROM homeduty.person
  WHERE family_id = p_family_id
  ORDER BY total_points ASC
  LIMIT 1;

  INSERT INTO homeduty.duty(family_id, task_id, assigned_to, due_date, status)
  VALUES (p_family_id, p_task_id, v_person_id, p_due, 'OPEN')
  RETURNING duty_id INTO v_duty_id;

  RETURN v_duty_id;
END;
$$;
