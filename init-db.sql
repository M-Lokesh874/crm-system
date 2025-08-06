-- Initialize CRM consolidated database
CREATE DATABASE crm_db;

-- Grant permissions to crm_user
GRANT ALL PRIVILEGES ON DATABASE crm_db TO crm_user;

-- Connect to the crm_db database
\c crm_db;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Initialize default roles for CRM system
INSERT INTO roles (name, description, created_at, updated_at) VALUES
('ADMIN', 'System Administrator with full access', NOW(), NOW()),
('SALES_MANAGER', 'Sales Manager with team management capabilities', NOW(), NOW()),
('SALES_REP', 'Sales Representative with customer management', NOW(), NOW()),
('SUPPORT_REP', 'Support Representative for customer support', NOW(), NOW())
ON CONFLICT (name) DO NOTHING; 