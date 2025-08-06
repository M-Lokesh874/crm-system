-- Initialize default roles for CRM system
-- This script should be run after the database is created

-- Connect to the crm_db database
\c crm_db;

-- Insert default roles
INSERT INTO roles (name, description, created_at, updated_at) VALUES
('ADMIN', 'System Administrator with full access', NOW(), NOW()),
('SALES_MANAGER', 'Sales Manager with team management capabilities', NOW(), NOW()),
('SALES_REP', 'Sales Representative with customer management', NOW(), NOW()),
('SUPPORT_REP', 'Support Representative for customer support', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- Verify roles were created
SELECT * FROM roles; 