CREATE OR REPLACE FUNCTION fn_open_duties_summary(p_person_id INT)
RETURNS TABLE(duty_id INT, task_name TEXT, due_date DATE)
LANGUAGE plpgsql
AS $$
DECLARE
  rec RECORD;
  cur CURSOR FOR
    SELECT d.duty_id, t.name AS task_name, d.due_date
    FROM homeduty.duty d
    JOIN homeduty.task t ON t.task_id = d.task_id
    WHERE d.assigned_to = p_person_id AND d.status='OPEN'
    ORDER BY d.due_date;
BEGIN
  OPEN cur;
  LOOP
    FETCH cur INTO rec;   -- record kullanımı
    EXIT WHEN NOT FOUND;
    duty_id := rec.duty_id;
    task_name := rec.task_name;
    due_date := rec.due_date;
    RETURN NEXT;
  END LOOP;
  CLOSE cur;
END;
$$;
--select  fn_open_duties_summary(16); 