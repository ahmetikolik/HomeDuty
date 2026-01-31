-- HOME DUTY - (family_id = 3)

INSERT INTO homeduty.family(name)
VALUES ('Yilmaz Family')
ON CONFLICT (name) DO NOTHING;

SELECT family_id, name
FROM homeduty.family
WHERE name = 'Yilmaz Family';

INSERT INTO homeduty.person(family_id, full_name, email, role_type, total_points)
VALUES
(3,'Ali Yilmaz','ali@mail.com','admin',0),
(3,'Ayse Yilmaz','ayse@mail.com','user',0),
(3,'Mehmet Yilmaz','mehmet@mail.com','user',0),
(3,'Elif Yilmaz','elif@mail.com','user',0)
ON CONFLICT (email) DO NOTHING;

INSERT INTO homeduty.task(name, base_points, is_weekly) VALUES
('Cop Atma',10,true),
('Market',15,true),
('Fatura Odeme',20,false),
('Bulasicik',8,true),
('Yer Silme',12,true),
('Camasir',10,true),
('Kedi Bakimi',6,true),
('Yemek',18,true),
('Oda Toplama',7,true),
('Araba Yikama',25,false)
ON CONFLICT (name) DO NOTHING;

INSERT INTO homeduty.badge(name, min_points) VALUES
('Bronz',20),
('Gumus',50),
('Altin',100)
ON CONFLICT (name) DO NOTHING;

-- Görev atama (isme göre)
SELECT homeduty.fn_assign_duty_by_name(3, 'Cop Atma', current_date);
SELECT homeduty.fn_assign_duty_by_name(3, 'Market', current_date + 1);
SELECT homeduty.fn_assign_duty_by_name(3, 'Fatura Odeme', current_date + 2);
SELECT homeduty.fn_assign_duty_by_name(3, 'Bulasicik', current_date + 3);

-- VIEW göster
SELECT * FROM homeduty.v_open_duties ORDER BY due_date;

-- Trigger test: bir OPEN görevi DONE yap
UPDATE homeduty.duty
SET status = 'DONE'
WHERE duty_id = (
  SELECT duty_id
  FROM homeduty.duty
  WHERE family_id = 3 AND status = 'OPEN'
  ORDER BY duty_id
  LIMIT 1
);
SELECT column_name
FROM information_schema.columns
WHERE table_schema='homeduty' AND table_name='person'
ORDER BY ordinal_position;
ALTER DATABASE homeduty SET search_path TO homeduty, public;

-- Log + puan kontrol
SELECT * FROM homeduty.duty_log ORDER BY done_at DESC;
SELECT person_id, full_name, total_points
FROM homeduty.person
WHERE family_id=3
ORDER BY total_points DESC;

-- Task ID kontrol (isteğe bağlı)
SELECT task_id, name, base_points
FROM homeduty.task
ORDER BY task_id;
