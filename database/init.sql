CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(255),
    provider VARCHAR(50) DEFAULT 'local',
    provider_id VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subjects (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    deadline DATE NOT NULL,
    required_hours NUMERIC(6, 2) NOT NULL,
    completed_hours NUMERIC(6, 2) NOT NULL DEFAULT 0,
    CONSTRAINT subjects_deadline_after_start CHECK (deadline >= start_date),
    CONSTRAINT subjects_required_hours_positive CHECK (required_hours > 0),
    CONSTRAINT subjects_completed_hours_non_negative CHECK (completed_hours >= 0)
);

CREATE INDEX IF NOT EXISTS idx_subjects_user_id ON subjects(user_id);