#!/bin/bash

# Install PostgreSQL (if not already installed)
echo "Setting up PostgreSQL database..."

# Login to PostgreSQL and execute the setup script
psql -U divakar-pt8008 -d postgres -f database-setup.sql

echo "Database setup completed!"
