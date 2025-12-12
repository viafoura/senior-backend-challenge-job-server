CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL
);

CREATE TABLE projects (
    id UUID PRIMARY KEY,
    name VARCHAR(100),
    owner_id UUID REFERENCES users(id)
);

CREATE TABLE jobs (
    id UUID PRIMARY KEY,
    params TEXT,
    status VARCHAR(20),
    result TEXT,
    error TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL REFERENCES users(id),
    project_id UUID REFERENCES projects(id)
);

-- Sample data
INSERT INTO users(id, username) VALUES
    ('11111111-1111-1111-1111-111111111111','alice'),
    ('22222222-2222-2222-2222-222222222222','bob');

INSERT INTO projects(id, name, owner_id) VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','demo','11111111-1111-1111-1111-111111111111');
