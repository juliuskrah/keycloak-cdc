#!/bin/bash
set -e

{ echo "host replication $POSTGRES_USER 0.0.0.0/0 trust"; } >> "$PGDATA/pg_hba.conf"

echo "Creating user and database 'sample, app'"
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER sample WITH PASSWORD 'sample';
	CREATE DATABASE sample;
	GRANT ALL PRIVILEGES ON DATABASE sample TO sample;
	\c sample;
	CREATE SCHEMA IF NOT EXISTS AUTHORIZATION sample;

    CREATE USER app WITH PASSWORD 'app';
	CREATE DATABASE app;
	GRANT ALL PRIVILEGES ON DATABASE app TO app;
	\c app;
	CREATE SCHEMA IF NOT EXISTS AUTHORIZATION app;
EOSQL
