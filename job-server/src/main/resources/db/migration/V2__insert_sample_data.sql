-- V2__insert_sample_data.sql
-- Sample data for users and projects

-- Insert sample users
INSERT INTO users (id, username, email, created_at, updated_at) VALUES
(1, 'john_doe', 'john.doe@example.com', NOW(), NOW()),
(2, 'jane_smith', 'jane.smith@example.com', NOW(), NOW()),
(3, 'bob_wilson', 'bob.wilson@example.com', NOW(), NOW()),
(4, 'alice_johnson', 'alice.johnson@example.com', NOW(), NOW()),
(5, 'charlie_brown', 'charlie.brown@example.com', NOW(), NOW());

-- Insert sample projects
INSERT INTO projects (id, name, description, owner_id, created_at, updated_at) VALUES
(1, 'Data Analysis Project', 'Project for analyzing sales data', 1, NOW(), NOW()),
(2, 'Machine Learning Pipeline', 'ML model training and inference pipeline', 2, NOW(), NOW()),
(3, 'Report Generation', 'Automated report generation system', 1, NOW(), NOW()),
(4, 'Image Processing', 'Batch image processing and transformation', 3, NOW(), NOW()),
(5, 'ETL Pipeline', 'Extract, Transform, Load data pipeline', 4, NOW(), NOW());
