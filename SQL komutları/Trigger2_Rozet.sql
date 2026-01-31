CREATE OR REPLACE FUNCTION trg_award_badges()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
  v_pid INT;
BEGIN
  -- duty_log insert edildiğinde ilgili kişinin rozetlerini kontrol et
  SELECT assigned_to INTO v_pid FROM homeduty.duty WHERE duty_id = NEW.duty_id;

  INSERT INTO homeduty.person_badge(person_id, badge_id)
  SELECT v_pid, b.badge_id
  FROM homeduty.badge b
  JOIN homeduty.person p ON p.person_id = v_pid
  WHERE p.total_points >= b.min_points
  ON CONFLICT DO NOTHING;

  RAISE NOTICE 'TRIGGER: Badges checked for person %', v_pid;
  RETURN NEW;
END;
$$;

CREATE TRIGGER tg_award_badges
AFTER INSERT ON homeduty.duty_log
FOR EACH ROW
EXECUTE FUNCTION trg_award_badges();
