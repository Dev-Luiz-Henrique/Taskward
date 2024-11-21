-- ================================================================
-- Inserts for Taskward Database
-- ================================================================

-- ================================================================
-- MAIN USER
-- ================================================================
INSERT INTO users (name, points)
VALUES ('Maria Oliveira', 50);

SELECT last_insert_rowid() AS user_id;

-- ================================================================
-- ----------------------------------------------------------------
-- ================================================================

-- ================================================================
-- DAILY TASK - UNIQUE AND INDEFINITE DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the daily task: "Drink 2L of water every day"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'water-icon.png', 'Drink 2L of water every day',
  'Drink at least 2 liters of water daily to stay hydrated',
  'daily', 1, CURRENT_TIMESTAMP, NULL, 10
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for today
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT last_insert_rowid() FROM tasks), 
  (SELECT last_insert_rowid() FROM tasks), 
  DATE('now'), 10, 'scheduled'
);

-- ================================================================
-- WEEKLY TASK - UNIQUE AND INDEFINITE DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the weekly task: "Weekly team meeting"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'meeting-icon.png', 'Weekly team meeting',
  'Attend the weekly team meeting to discuss projects and status.',
  'weekly', 1, CURRENT_TIMESTAMP, NULL, 15
);
SELECT last_insert_rowid() AS task_id;

-- Insert the next scheduled event for the upcoming Monday
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT last_insert_rowid() FROM tasks),
  (SELECT last_insert_rowid() FROM tasks),
  DATE('now', 'next monday'), 15, 'scheduled'
);

-- ================================================================
-- MONTHLY TASK - UNIQUE AND INDEFINITE DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the monthly task: "Credit card bill payment"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'credit-card-icon.png', 'Credit card bill payment',
  'Pay the credit card bill to avoid interest and keep the available limit.',
  'monthly', 1, CURRENT_TIMESTAMP, NULL, 20
);
SELECT last_insert_rowid() AS task_id;

-- Insert the next scheduled event for the 15th of the current month
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT last_insert_rowid() FROM tasks),
  (SELECT last_insert_rowid() FROM tasks),
  DATE('now', 'start of month', '+14 days'),
  20, 'scheduled'
);

-- ================================================================
-- YEARLY TASK - UNIQUE AND INDEFINITE DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the yearly task: "Annual goal review"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'goal-icon.png', 'Annual goal review',
  'Review the personal and professional goals set for the year.',
  'yearly', 1, CURRENT_TIMESTAMP, NULL, 30
);
SELECT last_insert_rowid() AS task_id;

-- Insert the next scheduled event for the first day of the next year
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT last_insert_rowid() FROM tasks),
  (SELECT last_insert_rowid() FROM tasks),
  DATE('now', 'start of year', '+1 year'),
  30, 'scheduled'
);

-- ================================================================
-- ----------------------------------------------------------------
-- ================================================================

-- ================================================================
-- DAILY TASK - UNIQUE AND DEFINED DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the daily task: "Daily exercise"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'exercise-icon.png', 'Daily exercise',
  'Perform daily physical exercise for 30 minutes.',
  'daily', 1, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+10 days'), 10
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for today
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE('now'), 10, 'scheduled'
);

-- ================================================================
-- WEEKLY TASK - UNIQUE AND DEFINED DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the weekly task: "Weekly meeting"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'weekly-meeting-icon.png', 'Weekly meeting',
  'Attend the weekly team meeting to review progress.',
  'weekly', 1, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+2 months'), 15
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for the next Wednesday
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE('now', 'next Wednesday'),
  15, 'scheduled'
);

-- ================================================================
-- MONTHLY TASK - UNIQUE AND DEFINED DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the monthly task: "Monthly bill payment"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'bill-payment-icon.png', 'Monthly bill payment',
  'Pay the monthly bills to keep the services active.',
  'monthly', 1, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+1 year'), 20
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for the 1st day of the next month
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE(DATE('now', '+1 month'), 'start of month'),
  20, 'scheduled'
);

-- ================================================================
-- YEARLY TASK - UNIQUE AND DEFINED DURATION WITHOUT INTERVAL
-- ================================================================
-- Inserting the yearly task: "Annual goal review"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'yearly-goal-icon.png', 'Annual goal review',
  'Review and adjust personal and professional goals for the upcoming year.',
  'yearly', 1, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+5 years'), 30
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for February 10th of next year
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE(DATE('now', '+1 year'), '+1 month', 'start of month', '+9 days'),
  30, 'scheduled'
);

-- ================================================================
-- ----------------------------------------------------------------
-- ================================================================

-- ================================================================
-- DAILY TASK - UNIQUE AND DEFINED DURATION WITH INTERVAL
-- ================================================================
-- Inserting the daily task: "Drink water regularly"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'hydration-icon.png', 'Drink water regularly',
  'Drink 2L of water every 3 days to maintain hydration.',
  'daily', 3, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+1 month'), 10
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for today
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE('now'), 10, 'scheduled'
);

-- ================================================================
-- WEEKLY TASK - UNIQUE AND DEFINED DURATION WITH INTERVAL
-- ================================================================
-- Inserting the weekly task: "Project review"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'project-review-icon.png', 'Project review',
  'Review the project progress every 4 weeks.',
  'weekly', 4, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+6 months'), 20
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for today
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE('now'), 20, 'scheduled'
);

-- ================================================================
-- MONTHLY TASK - UNIQUE AND DEFINED DURATION WITH INTERVAL
-- ================================================================
-- Inserting the monthly task: "Billing review"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'billing-review-icon.png', 'Billing review',
  'Review the credit card bill every 2 months to avoid interest.',
  'monthly', 2, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+1 year'), 30
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for today
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE('now'), 30, 'scheduled'
);

-- ================================================================
-- YEARLY TASK - UNIQUE AND DEFINED DURATION WITH INTERVAL
-- ================================================================
-- Inserting the yearly task: "Long-term goal review"
INSERT INTO tasks (icon, title, description, frequency, frequency_interval, start_date, end_date, points_reward)
VALUES (
  'long-term-goal-icon.png', 'Long-term goal review',
  'Review long-term goals every 2 years.',
  'yearly', 2, CURRENT_TIMESTAMP, DATE(CURRENT_TIMESTAMP, '+6 years'), 50
);
SELECT last_insert_rowid() AS task_id;

-- Insert the first scheduled event for today
INSERT INTO task_events (user_id, task_id, scheduled_date, points_earned, status)
VALUES (
  (SELECT id FROM users WHERE name = 'Maria Oliveira'),
  (SELECT last_insert_rowid()),
  DATE('now'), 50, 'scheduled'
);

-- ================================================================
-- ----------------------------------------------------------------
-- ================================================================
