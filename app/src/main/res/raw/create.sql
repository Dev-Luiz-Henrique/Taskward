-- ================================================================
-- Database Schema for Taskward
-- ================================================================

-- Creating the `users` table
CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE,
  photo TEXT,
  points INTEGER DEFAULT 0 CHECK (points >= 0),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Creating the `tasks` table
CREATE TABLE IF NOT EXISTS tasks (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  icon TEXT NOT NULL,
  title TEXT NOT NULL,
  description TEXT,
  frequency TEXT CHECK(frequency IN ('daily', 'weekly', 'monthly', 'yearly')) NOT NULL,
  frequency_interval INTEGER DEFAULT 1 CHECK (frequency_interval >= 1),
  start_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  end_date DATETIME,
  points_reward INTEGER DEFAULT 0 CHECK (points_reward >= 0),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Creating the `task_events` table
CREATE TABLE IF NOT EXISTS task_events (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER,
  task_id INTEGER,
  scheduled_date DATETIME,
  completed_date DATETIME,
  points_earned INTEGER DEFAULT 0 CHECK (points_earned >= 0),
  status TEXT CHECK(status IN ('scheduled', 'completed', 'cancelled', 'expired')) DEFAULT 'scheduled',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (task_id) REFERENCES tasks(id)
);

-- Creating the `rewards` table
CREATE TABLE IF NOT EXISTS rewards (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER,
  icon TEXT NOT NULL,
  title TEXT NOT NULL,
  description TEXT,
  points_required INTEGER NOT NULL CHECK (points_required > 0),
  date_redeemed DATETIME,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
