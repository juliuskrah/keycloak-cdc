#!/bin/sh

# ---- Debezium connector for Keycloak
#
# This will create a connector that streams changes on PostgreSQL to Kafka
#
curl -s \
     -X "POST" "http://localhost:8083/connectors/" \
     -H "Content-Type: application/json" \
     -d '{
  "name": "keycloak-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "postgres",
    "database.port": "5432",
    "slot.name": "keycloak_debezium",
    "plugin.name": "pgoutput",
    "database.user": "julius",
    "database.password": "julius123",
    "database.dbname": "sample",
    "database.server.name": "keycloak",
    "schema.whitelist": "sample",
    "table.whitelist": "sample.user_role_mapping,sample.user_group_membership,sample.keycloak_role,sample.user_entity"
  }
}'