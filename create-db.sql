-- Create the CRM database
CREATE DATABASE crm_db;

-- Connect to the crm_db database
\c crm_db;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Note: Tables will be created automatically by JPA when the application starts
-- with ddl-auto: create-drop or ddl-auto: update 