--  Temizlik
DROP SCHEMA IF EXISTS homeduty CASCADE;
CREATE SCHEMA homeduty;
SET search_path TO homeduty;

--  SEQUENCE 
CREATE SEQUENCE seq_duty_no START 1000;

--  Tablolar

CREATE TABLE family (
  family_id SERIAL PRIMARY KEY,
  name        VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE person (
  person_id  SERIAL PRIMARY KEY,
  family_id  INT NOT NULL REFERENCES family(family_id) ON DELETE CASCADE,
  full_name  VARCHAR(80) NOT NULL,
  email      VARCHAR(120) UNIQUE,
  role_type  VARCHAR(10) NOT NULL CHECK (role_type IN ('admin','user')),
  total_points INT NOT NULL DEFAULT 0 CHECK (total_points >= 0)   -- sayı kısıtı
);

CREATE TABLE task (
  task_id  SERIAL PRIMARY KEY,
  name     VARCHAR(80) NOT NULL UNIQUE,
  base_points INT NOT NULL CHECK (base_points BETWEEN 1 AND 50), -- sayı kısıtı
  is_weekly BOOLEAN NOT NULL DEFAULT TRUE
);

-- Atanmış görev
CREATE TABLE duty (
  duty_id      INT PRIMARY KEY DEFAULT nextval('seq_duty_no'), -- sequence kullanımı
  family_id    INT NOT NULL REFERENCES family(family_id) ON DELETE CASCADE,
  task_id      INT NOT NULL REFERENCES task(task_id) ON DELETE RESTRICT, -- silme kısıtı (RESTRICT)
  assigned_to  INT NOT NULL REFERENCES person(person_id) ON DELETE RESTRICT, -- silme kısıtı
  due_date     DATE NOT NULL,
  status       VARCHAR(12) NOT NULL CHECK (status IN ('OPEN','DONE','CANCELLED')),
  created_at   TIMESTAMP NOT NULL DEFAULT now()
);

-- Tamamlama / puan logu (trigger ile dolduracağız)
CREATE TABLE duty_log (
  log_id    SERIAL PRIMARY KEY,
  duty_id   INT NOT NULL UNIQUE REFERENCES duty(duty_id) ON DELETE CASCADE,
  done_at   TIMESTAMP NOT NULL DEFAULT now(),
  points_awarded INT NOT NULL CHECK (points_awarded >= 0),
  note      VARCHAR(200)
);

-- Rozetler
CREATE TABLE badge (
  badge_id SERIAL PRIMARY KEY,
  name     VARCHAR(60) NOT NULL UNIQUE,
  min_points INT NOT NULL CHECK (min_points > 0)
);

CREATE TABLE person_badge (
  person_id INT NOT NULL REFERENCES person(person_id) ON DELETE CASCADE,
  badge_id  INT NOT NULL REFERENCES badge(badge_id) ON DELETE CASCADE,
  earned_at TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY(person_id, badge_id)
);

--  INDEX 
-- Arayüzde "isime göre kişi ara" veya "email ara" yaptığında bunu kullanacağız
CREATE INDEX idx_person_fullname ON person(full_name);
CREATE INDEX idx_duty_due_date ON duty(due_date);
CREATE INDEX idx_duty_assigned_to_status ON duty(assigned_to, status);

--  VIEW 
CREATE OR REPLACE VIEW v_open_duties AS
SELECT d.duty_id, f.name AS family, t.name AS task, p.full_name AS assigned_to,
       d.due_date, d.status
FROM duty d
JOIN family f ON f.family_id = d.family_id
JOIN task t   ON t.task_id = d.task_id
JOIN person p ON p.person_id = d.assigned_to
WHERE d.status = 'OPEN';

--  UNION 
-- "Bugün bitecek açık görevler" + "Gecikmiş açık görevler" birleşik liste
CREATE OR REPLACE VIEW v_today_and_overdue_open AS
SELECT d.duty_id, 'TODAY' AS bucket, d.due_date, p.full_name, t.name AS task
FROM duty d
JOIN person p ON p.person_id = d.assigned_to
JOIN task t ON t.task_id = d.task_id
WHERE d.status='OPEN' AND d.due_date = current_date
UNION
SELECT d.duty_id, 'OVERDUE' AS bucket, d.due_date, p.full_name, t.name AS task
FROM duty d
JOIN person p ON p.person_id = d.assigned_to
JOIN task t ON t.task_id = d.task_id
WHERE d.status='OPEN' AND d.due_date < current_date;

-- 6) Aggregate + HAVING 
-- "En az 20 puan toplamış kişileri listele"
CREATE OR REPLACE VIEW v_high_performers AS
SELECT p.person_id, p.full_name, SUM(l.points_awarded) AS sum_points, COUNT(*) AS done_count
FROM person p
JOIN duty d ON d.assigned_to = p.person_id
JOIN duty_log l ON l.duty_id = d.duty_id
GROUP BY p.person_id, p.full_name
HAVING SUM(l.points_awarded) >= 20;

