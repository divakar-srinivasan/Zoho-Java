-- Create database
CREATE DATABASE IF NOT EXISTS test;

-- Switch to test database
\c test;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create resources table
CREATE TABLE IF NOT EXISTS resources (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO users (id, username, password, email) VALUES
('user1', 'admin', 'admin', 'admin@example.com'),
('user2', 'john', 'john123', 'john@example.com');

INSERT INTO resources (id, name, description) VALUES
('res1', 'Database Server', 'PostgreSQL database server'),
('res2', 'Web Server', 'Apache Tomcat web server'),
('res3', 'File Storage', 'Central file storage system');

-- Create indexes
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_resource_id ON resources(id);
