CREATE OR REPLACE FUNCTION trg_duty_done_points()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
  v_points INT;
BEGIN
  IF NEW.status = 'DONE' AND OLD.status <> 'DONE' THEN
    SELECT base_points INTO v_points FROM homeduty.task WHERE task_id = NEW.task_id;

    INSERT INTO homeduty.duty_log(duty_id, points_awarded, note)
    VALUES (NEW.duty_id, v_points, 'Auto log by trigger');

    UPDATE homeduty.person
    SET total_points = total_points + v_points
    WHERE person_id = NEW.assigned_to;

    
    RAISE NOTICE 'TRIGGER: Duty % DONE -> % points awarded', NEW.duty_id, v_points;
  END IF;

  RETURN NEW;
END;
$$;

CREATE TRIGGER tg_duty_done_points
AFTER UPDATE OF status ON homeduty.duty
FOR EACH ROW
EXECUTE FUNCTION trg_duty_done_points();
 select  * from duty_log; 