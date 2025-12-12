-- Create tables
CREATE TABLE IF NOT EXISTS users (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS projects (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id CHAR(36) NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS jobs (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    project_id CHAR(36),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    result_value INTEGER,
    result_external_status VARCHAR(50),
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (project_id) REFERENCES projects(id)
    );

CREATE TABLE IF NOT EXISTS tasks (
    id CHAR(36) PRIMARY KEY,
    job_id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    metadata JSON,
    FOREIGN KEY (job_id) REFERENCES jobs(id)
    );

-- Create indexes
CREATE INDEX idx_jobs_user_id ON jobs(user_id);
CREATE INDEX idx_jobs_project_id ON jobs(project_id);
CREATE INDEX idx_jobs_status ON jobs(status);
CREATE INDEX idx_tasks_job_id ON tasks(job_id);